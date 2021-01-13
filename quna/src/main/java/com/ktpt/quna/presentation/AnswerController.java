package com.ktpt.quna.presentation;

import com.ktpt.quna.application.AnswerService;
import com.ktpt.quna.application.dto.AnswerRequest;
import com.ktpt.quna.application.dto.AnswerResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/questions/{questionId}/answers")
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping
    public ResponseEntity<AnswerResponse> create(@RequestBody AnswerRequest request, @PathVariable Long questionId) {
        AnswerResponse response = answerService.create(request, questionId);

        return ResponseEntity.created(URI.create("/questions/" + questionId + "/answers/" + response.getId()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<AnswerResponse>> findAll(@PathVariable Long questionId) {
        List<AnswerResponse> responses = answerService.findAll(questionId);
        return ResponseEntity.ok(responses);
    }
}
