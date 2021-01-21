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

import com.ktpt.quna.application.QuestionService;
import com.ktpt.quna.application.dto.QuestionRequest;
import com.ktpt.quna.application.dto.QuestionResponse;
import com.ktpt.quna.infra.annotation.LoginRequired;

@RequestMapping("/questions")
@RestController
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @LoginRequired
    @PostMapping
    public ResponseEntity<QuestionResponse> create(@RequestBody @Valid QuestionRequest request) {
        QuestionResponse response = questionService.create(request);
        return ResponseEntity.created(URI.create("/questions/" + response.getId()))
            .body(response);
    }

    @LoginRequired
    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponse> update(@PathVariable Long id, @RequestBody @Valid QuestionRequest request) {
        QuestionResponse response = questionService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponse> findById(@PathVariable Long id) {
        QuestionResponse response = questionService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<QuestionResponse>> findAll() {
        List<QuestionResponse> response = questionService.findAll();
        return ResponseEntity.ok(response);
    }

    @LoginRequired
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        questionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
