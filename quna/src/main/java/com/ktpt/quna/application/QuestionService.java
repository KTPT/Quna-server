package com.ktpt.quna.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ktpt.quna.application.dto.QuestionRequest;
import com.ktpt.quna.application.dto.QuestionResponse;
import com.ktpt.quna.application.exception.NotFoundException;
import com.ktpt.quna.domain.model.Question;
import com.ktpt.quna.domain.model.QuestionRepository;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Transactional
    public QuestionResponse create(QuestionRequest request) {
        Question question = request.toEntity();
        Question saved = questionRepository.save(question.create());

        return QuestionResponse.from(saved);
    }

    public QuestionResponse findById(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 Question, id = " + id));

        return QuestionResponse.from(question);
    }

    public List<QuestionResponse> findAll() {
        return QuestionResponse.listOf(questionRepository.findAll());
    }
}
