package com.ktpt.quna;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.ktpt.quna.application.dto.QuestionRequest;
import com.ktpt.quna.application.dto.QuestionResponse;
import com.ktpt.quna.application.exception.ErrorResponse;
import com.ktpt.quna.domain.model.Question;
import com.ktpt.quna.domain.model.QuestionRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class QuestionTests {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private QuestionRepository repository;

    @Autowired
    private WebApplicationContext applicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
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
    void request_WhenEmptyTitleAndContents_ThenThrowException() throws Exception {
        String emptyTitle = "";
        String emptyContents = "";
        String body = objectMapper.writeValueAsString(new QuestionRequest(emptyTitle, emptyContents, null));

        MvcResult result = mockMvc.perform(post("/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse()
                .getContentAsString();

        ErrorResponse response = objectMapper.readValue(responseBody, ErrorResponse.class);

        assertThat(response.getMessage()).contains("2 errors");
    }

    @Test
    void update() throws Exception {
        Question saved = repository.save(
                new Question(null, "title", "contents", null, LocalDateTime.now(), LocalDateTime.now()));

        String updatedTitle = "title1";
        String updatedContents = "contents1";
        String body = objectMapper.writeValueAsString(new QuestionRequest(updatedTitle, updatedContents, null));

        MvcResult result = mockMvc.perform(put("/questions/" + saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
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
    void update_WhenNotExist_ThenThrowException() throws Exception {
        String updatedTitle = "title1";
        String updatedContents = "contents1";
        String body = objectMapper.writeValueAsString(new QuestionRequest(updatedTitle, updatedContents, null));

        int notExistId = -1;

        MvcResult result = mockMvc.perform(put("/questions/" + notExistId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isNotFound())
                .andReturn();

        String responseBody = result.getResponse()
                .getContentAsString();

        ErrorResponse response = objectMapper.readValue(responseBody, ErrorResponse.class);

        assertThat(response.getMessage()).isEqualTo("존재하지 않는 Question, id = " + notExistId);
    }

    @Test
    void update_WhenSameProperties_ThenThrowException() throws Exception {
        String sameTitle = "title";
        String sameContents = "contents";
        Long sameResponderId = 1L;

        Question saved = createFixture(sameTitle, sameContents, sameResponderId);

        String body = objectMapper.writeValueAsString(new QuestionRequest(sameTitle, sameContents, sameResponderId));

        MvcResult result = mockMvc.perform(put("/questions/" + saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse()
                .getContentAsString();

        ErrorResponse response = objectMapper.readValue(responseBody, ErrorResponse.class);

        assertThat(response.getMessage()).isEqualTo("동일한 내용으로 수정할 수 없습니다.");
    }

    @Test
    void findById() throws Exception {
        String title = "title";
        String contents = "contents";
        Question saved = repository.save(
                new Question(null, title, contents, null, LocalDateTime.now(), LocalDateTime.now()));

        MvcResult result = mockMvc.perform(get("/questions/{id}", saved.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
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
                .andExpect(status().isNoContent());

        assertThat(repository.findById(fixture.getId())).isNotPresent();
    }

    @Test
    void delete_WhenNotExist_ThenThrowException() throws Exception {
        Long notExistId = -1L;

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/questions/" + notExistId))
                .andExpect(status().isNotFound())
                .andReturn();

        String responseBody = result.getResponse()
                .getContentAsString();

        ErrorResponse response = objectMapper.readValue(responseBody, ErrorResponse.class);

        assertThat(response.getMessage()).contains("No class");
    }

    private Question createFixture(String title, String contents, Long responderId) {
        return repository.save(new Question(null, title, contents, responderId, null, null));
    }
}
