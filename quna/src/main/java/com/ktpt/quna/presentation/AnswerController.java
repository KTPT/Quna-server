package com.ktpt.quna.presentation;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ktpt.quna.application.AnswerService;
import com.ktpt.quna.application.dto.AnswerRequest;
import com.ktpt.quna.application.dto.AnswerResponse;
import com.ktpt.quna.infra.annotation.LoginRequired;
import com.ktpt.quna.presentation.verifier.QuestionShouldExist;

@QuestionShouldExist
@RequestMapping("/questions/{questionId}/answers")
@RestController
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @LoginRequired
    @PostMapping
    public ResponseEntity<AnswerResponse> create(@PathVariable Long questionId,
        @RequestBody @Valid AnswerRequest request) {
        AnswerResponse response = answerService.create(request, questionId);

        return ResponseEntity.created(URI.create("/questions/" + questionId + "/answers/" + response.getId()))
            .body(response);
    }

    @GetMapping
    public ResponseEntity<List<AnswerResponse>> findAll(@PathVariable Long questionId) {
        List<AnswerResponse> responses = answerService.findAll(questionId);
        return ResponseEntity.ok(responses);
    }

    /**
     @param questionId used for verification
     @see com.ktpt.quna.presentation.verifier.RequestAspect
     */
    @LoginRequired
    @PutMapping("/{id}")
    public ResponseEntity<AnswerResponse> update(@PathVariable Long questionId, @PathVariable Long id,
        @RequestBody @Valid AnswerRequest request) {
        AnswerResponse response = answerService.update(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     @param questionId used for verification
     @see com.ktpt.quna.presentation.verifier.RequestAspect
     */
    @LoginRequired
    @DeleteMapping("/{id}")
    public ResponseEntity<AnswerResponse> delete(@PathVariable Long questionId, @PathVariable Long id) {
        answerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
