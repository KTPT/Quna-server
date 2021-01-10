package com.ktpt.quna;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.ktpt.quna.application.dto.QuestionRequest;
import com.ktpt.quna.application.dto.QuestionResponse;
import com.ktpt.quna.domain.model.Question;
import com.ktpt.quna.domain.model.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class QuestionTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private QuestionRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void create() throws Exception {
        String title = "title";
        String contents = "contents";
        String body = objectMapper.writeValueAsString(new QuestionRequest(title, contents, null));

        MvcResult result = mockMvc.perform(post("/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, "/questions/1"))
                .andDo(print())
                .andReturn();

        String responseBody = result.getResponse()
                .getContentAsString();

        QuestionResponse response = objectMapper.readValue(responseBody, QuestionResponse.class);

        assertThat(response.getId()).isEqualTo(1);
        assertThat(response.getTitle()).isEqualTo(title);
        assertThat(response.getContents()).isEqualTo(contents);
        assertThat(response.getResponderId()).isNull();
        assertThat(response.getCreatedAt()).isNotNull();
        assertThat(response.getLastModifiedAt()).isNotNull();
    }

    @Test
    void update() throws Exception {
        Question saved = repository.save(new Question(null, "title", "contents", null, LocalDateTime.now(), LocalDateTime.now()));

        String updatedTitle = "title1";
        String updatedContents = "contents1";
        String body = objectMapper.writeValueAsString(new QuestionRequest(updatedTitle, updatedContents, null));

        MvcResult result = mockMvc.perform(put("/questions/" + saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String responseBody = result.getResponse()
                .getContentAsString();

        QuestionResponse response = objectMapper.readValue(responseBody, QuestionResponse.class);

        assertThat(response.getId()).isEqualTo(saved.getId());
        assertThat(response.getTitle()).isEqualTo(updatedTitle);
        assertThat(response.getContents()).isEqualTo(updatedContents);
        assertThat(response.getResponderId()).isNull();
        assertThat(response.getCreatedAt()).isNotNull();
        assertThat(response.getLastModifiedAt()).isNotEqualTo(saved.getLastModifiedAt().toString());
    }

    @Test
    void findById() throws Exception {
        String title = "title";
        String contents = "contents";
        Question saved = repository.save(new Question(null, title, contents, null, LocalDateTime.now(), LocalDateTime.now()));

        MvcResult result = mockMvc.perform(get("/questions/{id}", saved.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String responseBody = result.getResponse()
                .getContentAsString();

        QuestionResponse response = objectMapper.readValue(responseBody, QuestionResponse.class);

        assertThat(response.getId()).isEqualTo(saved.getId());
        assertThat(response.getTitle()).isEqualTo(title);
        assertThat(response.getContents()).isEqualTo(contents);
        assertThat(response.getResponderId()).isNull();
        assertThat(response.getCreatedAt()).isNotNull();
        assertThat(response.getLastModifiedAt()).isNotNull();
    }

    @Test
    void findAll() throws Exception {
        String title = "title";
        String contents = "contents";
        repository.save(new Question(null, title, contents, null, LocalDateTime.now(), LocalDateTime.now()));
        repository.save(new Question(null, title, contents, null, LocalDateTime.now(), LocalDateTime.now()));

        MvcResult result = mockMvc.perform(get("/questions")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String responseBody = result.getResponse()
                .getContentAsString();

        CollectionType collectionType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, QuestionResponse.class);
        List<QuestionResponse> response = objectMapper.readValue(responseBody, collectionType);

        assertThat(response.size()).isEqualTo(2);
    }

    @Test
    void delete() throws Exception {
        Question fixture = createFixture("test", "test", null);

        mockMvc.perform(MockMvcRequestBuilders.delete("/questions/" + fixture.getId()))
                .andExpect(status().isNoContent())
                .andDo(print());

        assertThat(repository.findById(fixture.getId())).isNotPresent();
    }

    private Question createFixture(String title, String contents, Long responderId) {
        return repository.save(new Question(null, title, contents, responderId, null, null));
    }
}
