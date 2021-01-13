package com.ktpt.quna.application;

import com.ktpt.quna.application.dto.AnswerRequest;
import com.ktpt.quna.application.dto.AnswerResponse;
import com.ktpt.quna.domain.model.Answer;
import com.ktpt.quna.domain.model.AnswerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnswerService {

    private final AnswerRepository repository;

    public AnswerService(AnswerRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public AnswerResponse create(AnswerRequest request, Long questionId) {
        Answer answer = request.toEntity(questionId);
        answer.create();
        Answer saved = repository.save(answer);

        return AnswerResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<AnswerResponse> findAll(Long questionId) {
        List<Answer> answers = repository.findAllByQuestionId(questionId);
        return AnswerResponse.listOf(answers);
    }
}
