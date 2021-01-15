package com.ktpt.quna;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.ktpt.quna.application.dto.AnswerRequest;
import com.ktpt.quna.application.dto.AnswerResponse;
import com.ktpt.quna.application.dto.QuestionResponse;
import com.ktpt.quna.domain.model.Answer;
import com.ktpt.quna.domain.model.AnswerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.matchesRegex;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AnswerTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AnswerRepository repository;

    @Autowired
    private WebApplicationContext applicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        repository.deleteAll();
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    void create() throws Exception {
        String contents = "answer contents";
        String requestBody = objectMapper.writeValueAsString(new AnswerRequest(contents));

        Long questionId = 1L;
        MvcResult result = mockMvc.perform(post("/questions/" + questionId + "/answers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, matchesRegex("/questions/\\d/answers/\\d")))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        AnswerResponse response = objectMapper.readValue(responseBody, AnswerResponse.class);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getQuestionId()).isEqualTo(questionId);
        assertThat(response.getContents()).isEqualTo(contents);
        assertThat(response.getCreatedAt()).isNotNull();
        assertThat(response.getLastModifiedAt()).isNotNull();

    }

    @Test
    public void findAll() throws Exception {
        List<Long> questionIds = Arrays.asList(1L, 2L);
        List<String> contents = Arrays.asList("contents1", "contents2", "contents3", "contents4");
        repository.save(new Answer(null, questionIds.get(0), contents.get(0), LocalDateTime.now(),
                LocalDateTime.now()));
        repository.save(new Answer(null, questionIds.get(0), contents.get(1), LocalDateTime.now(),
                LocalDateTime.now()));
        repository.save(new Answer(null, questionIds.get(1), contents.get(2), LocalDateTime.now(),
                LocalDateTime.now()));
        repository.save(new Answer(null, questionIds.get(1), contents.get(3), LocalDateTime.now(),
                LocalDateTime.now()));

        MvcResult result = mockMvc.perform(get("/questions/" + questionIds.get(1) + "/answers")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, QuestionResponse.class);
        List<QuestionResponse> responses = objectMapper.readValue(responseBody, collectionType);

        assertThat(responses.size()).isEqualTo(2);
        assertThat(responses.get(0).getContents()).isEqualTo(contents.get(2));
        assertThat(responses.get(1).getContents()).isEqualTo(contents.get(3));
    }

    @Test
    public void update() throws Exception {
        Answer saved = repository.save(new Answer(null, 1L, "contents", LocalDateTime.now(), LocalDateTime.now()));
        String updatedContents = "updatedContents";
        String requestBody = objectMapper.writeValueAsString(new AnswerRequest(updatedContents));

        MvcResult result = mockMvc.perform(put("/questions/" + saved.getQuestionId() + "/answers/" + saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse()
                .getContentAsString();
        AnswerResponse response = objectMapper.readValue(responseBody, AnswerResponse.class);

        assertThat(response.getId()).isEqualTo(saved.getId());
        assertThat(response.getQuestionId()).isEqualTo(saved.getQuestionId());
        assertThat(response.getContents()).isEqualTo(updatedContents);
        assertThat(response.getCreatedAt()).isNotNull();
        assertThat(response.getLastModifiedAt()).isNotEqualTo(saved.getLastModifiedAt().toString());
    }

    @Test
    public void delete() throws Exception {
        Answer saved = repository.save(new Answer(null, 1L, "contents", null, null));

        mockMvc.perform(MockMvcRequestBuilders.delete("/questions/" + saved.getQuestionId() + "/answers/" + saved.getId()))
                .andExpect(status().isNoContent());

        assertThat(repository.findById(saved.getId())).isNotPresent();
    }
}
