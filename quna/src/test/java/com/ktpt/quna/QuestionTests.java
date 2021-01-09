package com.ktpt.quna;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktpt.quna.application.dto.QuestionRequest;
import com.ktpt.quna.application.dto.QuestionResponse;
import com.ktpt.quna.domain.model.QuestionRepository;

import javax.print.attribute.standard.Media;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class QuestionTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private QuestionRepository repository;

    /*@BeforeEach
    void setUp() {
        repository.deleteAll();
    }*/

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
        String updatedTitle = "title1";
        String updatedContents = "contents1";
        String body = objectMapper.writeValueAsString(new QuestionRequest(updatedTitle, updatedContents, null));

        MvcResult result = mockMvc.perform(put("/questions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String responseBody = result.getResponse()
                .getContentAsString();

        QuestionResponse response = objectMapper.readValue(responseBody, QuestionResponse.class);

        assertThat(response.getId()).isEqualTo(1);
        assertThat(response.getTitle()).isEqualTo(updatedTitle);
        assertThat(response.getContents()).isEqualTo(updatedContents);
        assertThat(response.getResponderId()).isNull();
        assertThat(response.getCreatedAt()).isNotNull();
        assertThat(response.getLastModifiedAt()).isNotNull();
    }
}
