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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        Long answerId = 1L;
        MvcResult result = mockMvc.perform(post("/questions/" + questionId + "/answers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, "/questions/" + questionId + "/answers/" + answerId))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        AnswerResponse response = objectMapper.readValue(responseBody, AnswerResponse.class);

        assertThat(response.getId()).isEqualTo(answerId);
        assertThat(response.getQuestionId()).isEqualTo(questionId);
        assertThat(response.getContents()).isEqualTo(contents);
        assertThat(response.getCreatedAt()).isNotNull();
        assertThat(response.getLastModifiedAt()).isNotNull();

    }

    @Test
    public void findAll() throws Exception {
        Long[] questionId = {1L, 2L};
        String[] contents = {"contents1", "contents2", "contents3", "contents4"};
        repository.save(new Answer(null, questionId[0], contents[0], LocalDateTime.now(), LocalDateTime.now()));
        repository.save(new Answer(null, questionId[0], contents[1], LocalDateTime.now(), LocalDateTime.now()));
        repository.save(new Answer(null, questionId[1], contents[2], LocalDateTime.now(), LocalDateTime.now()));
        repository.save(new Answer(null, questionId[1], contents[3], LocalDateTime.now(), LocalDateTime.now()));

        MvcResult result = mockMvc.perform(get("/questions/" + questionId[1] + "/answers")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, QuestionResponse.class);
        List<QuestionResponse> responses = objectMapper.readValue(responseBody, collectionType);

        assertThat(responses.size()).isEqualTo(2);
    }

}
