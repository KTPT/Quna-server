package com.ktpt.quna;

import java.net.URI;
import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/questions")
@RestController
public class QuestionController {

    @PostMapping
    public ResponseEntity<QuestionResponse> create(@RequestBody QuestionRequest request) {
        QuestionResponse response = questionService.create(request);
        return ResponseEntity.created(URI.create("/questions/" + response.getId()))
                .body(response);
    }
}
