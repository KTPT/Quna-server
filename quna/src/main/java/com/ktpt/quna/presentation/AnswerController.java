package com.ktpt.quna.presentation;

import com.ktpt.quna.application.AnswerService;
import com.ktpt.quna.application.dto.AnswerRequest;
import com.ktpt.quna.application.dto.AnswerResponse;
import com.ktpt.quna.presentation.verifier.QuestionShouldExist;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@QuestionShouldExist
@RequestMapping("/questions/{questionId}/answers")
@RestController
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping
    public ResponseEntity<AnswerResponse> create(@PathVariable Long questionId, @RequestBody @Valid AnswerRequest request) {
        AnswerResponse response = answerService.create(request, questionId);

        return ResponseEntity.created(URI.create("/questions/" + questionId + "/answers/" + response.getId()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<AnswerResponse>> findAll(@PathVariable Long questionId) {
        List<AnswerResponse> responses = answerService.findAll(questionId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnswerResponse> update(@PathVariable Long questionId, @PathVariable Long id,
                                                 @RequestBody @Valid AnswerRequest request) {
        AnswerResponse response = answerService.update(questionId, id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AnswerResponse> delete(@PathVariable Long questionId, @PathVariable Long id) {
        answerService.delete(questionId, id);
        return ResponseEntity.noContent().build();
    }
}
