package com.ktpt.quna.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ktpt.quna.application.dto.QuestionRequest;
import com.ktpt.quna.application.dto.QuestionResponse;
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

    @Transactional
    public void delete(Long id) {
        // TODO: 2021/01/08 해당되는 id가 없을 경우에 발생하는 EmptyResultDataAccessException에 대해 추후 advice로 처리한다. 
        questionRepository.deleteById(id);
    }
}
