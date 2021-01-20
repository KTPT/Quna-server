package com.ktpt.quna;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.ktpt.quna.application.dto.AnswerRequest;
import com.ktpt.quna.application.dto.AnswerResponse;
import com.ktpt.quna.application.dto.QuestionResponse;
import com.ktpt.quna.domain.model.Answer;
import com.ktpt.quna.domain.model.AnswerRepository;
import com.ktpt.quna.domain.model.Member;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.ktpt.quna.application.dto.AnswerRequest;
import com.ktpt.quna.application.dto.AnswerResponse;
import com.ktpt.quna.application.dto.QuestionResponse;
import com.ktpt.quna.application.exception.ErrorResponse;
import com.ktpt.quna.domain.model.Answer;
import com.ktpt.quna.domain.model.AnswerRepository;
import com.ktpt.quna.domain.model.Question;
import com.ktpt.quna.domain.model.QuestionRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AnswerTests extends AuthTestStep {
    private static final String AUTHORIZATION = "Authorization";
    private String token;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private WebApplicationContext applicationContext;

    private Question question;
    private Question question1;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        answerRepository.deleteAll();
        questionRepository.deleteAll();
        question = questionRepository.save(
                new Question(null, "title", "contents", null, LocalDateTime.now(), LocalDateTime.now()));
        question1 = questionRepository.save(
                new Question(null, "title", "contents", null, LocalDateTime.now(), LocalDateTime.now()));

        clearMember();
        Member member = createDefaultMember();
        token = createToken(member.getId());

        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    void create() throws Exception {
        String contents = "answer contents";
        String requestBody = objectMapper.writeValueAsString(new AnswerRequest(contents));

        Long questionId = question.getId();
        MvcResult result = mockMvc.perform(post("/questions/" + questionId + "/answers")
                .header(AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, matchesRegex("/questions/\\d*/answers/\\d*")))
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
    void findAll() throws Exception {

        List<Long> questionIds = Arrays.asList(question.getId(), question1.getId());
        List<String> contents = Arrays.asList("contents1", "contents2", "contents3", "contents4");
        answerRepository.save(new Answer(null, questionIds.get(0), contents.get(0), LocalDateTime.now(),
                LocalDateTime.now()));
        answerRepository.save(new Answer(null, questionIds.get(0), contents.get(1), LocalDateTime.now(),
                LocalDateTime.now()));
        answerRepository.save(new Answer(null, questionIds.get(1), contents.get(2), LocalDateTime.now(),
                LocalDateTime.now()));
        answerRepository.save(new Answer(null, questionIds.get(1), contents.get(3), LocalDateTime.now(),
                LocalDateTime.now()));

        MvcResult result = mockMvc.perform(get("/questions/" + questionIds.get(1) + "/answers")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        CollectionType collectionType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, QuestionResponse.class);
        List<QuestionResponse> responses = objectMapper.readValue(responseBody, collectionType);

        assertThat(responses.size()).isEqualTo(2);
        assertThat(responses.get(0).getContents()).isEqualTo(contents.get(2));
        assertThat(responses.get(1).getContents()).isEqualTo(contents.get(3));
    }

    @Test
    public void update() throws Exception {
        Answer saved = answerRepository.save(
                new Answer(null, question.getId(), "contents", LocalDateTime.now(), LocalDateTime.now()));

        String updatedContents = "updatedContents";
        String requestBody = objectMapper.writeValueAsString(new AnswerRequest(updatedContents));

        MvcResult result = mockMvc.perform(put("/questions/" + question.getId() + "/answers/" + saved.getId())
                .header(AUTHORIZATION, token)
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
        Answer saved = answerRepository.save(new Answer(null, question.getId(), "contents", null, null));

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/questions/" + saved.getQuestionId() + "/answers/" + saved.getId())
                .header(AUTHORIZATION, token))
                .andExpect(status().isNoContent());

        assertThat(questionRepository.findById(saved.getId())).isNotPresent();
    }

    @Test
    void request_WhenQuestionNotExist_ThenThrowException() throws Exception {
        String body = objectMapper.writeValueAsString(new AnswerRequest("contents"));

        MvcResult result = mockMvc.perform(post("/questions/-1/answers")
                .header(AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse()
                .getContentAsString();

        ErrorResponse response = objectMapper.readValue(responseBody, ErrorResponse.class);

        assertThat(response.getMessage()).isEqualTo("존재하지 않는 question, id: -1");
    }

    @Test
    void update_WhenSameContents_ThenThrowException() throws Exception {
        String contents = "contents";
        Answer saved = answerRepository.save(
                new Answer(null, question.getId(), contents, LocalDateTime.now(), LocalDateTime.now()));

        String body = objectMapper.writeValueAsString(new AnswerRequest(contents));

        MvcResult result = mockMvc.perform(put("/questions/{questionId}/answers/{answerId}",
                question.getId(), saved.getId())
                .header(AUTHORIZATION, token)
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
    void request_WhenEmptyContents_ThenThrowException() throws Exception {
        String emptyContents = "";
        String body = objectMapper.writeValueAsString(new AnswerRequest(emptyContents));

        MvcResult result = mockMvc.perform(post("/questions/{questionId}/answers", question.getId())
                .header(AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse()
                .getContentAsString();

        ErrorResponse response = objectMapper.readValue(responseBody, ErrorResponse.class);

        assertThat(response.getMessage()).isEqualTo("must not be blank");
    }

    @Test
    void delete_WhenNotExist_ThenThrowException() throws Exception {
        long notExistId = -1L;

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/questions/{questionId}/answers/{answerId}",
                        question.getId(), notExistId)
                .header(AUTHORIZATION, token))
                .andExpect(status().isNotFound())
                .andReturn();

        String responseBody = result.getResponse()
                .getContentAsString();

        ErrorResponse response = objectMapper.readValue(responseBody, ErrorResponse.class);

        assertThat(response.getMessage()).contains("No class");
    }
}
