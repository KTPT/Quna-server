package com.ktpt.quna.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ktpt.quna.application.dto.AnswerRequest;
import com.ktpt.quna.application.dto.AnswerResponse;
import com.ktpt.quna.application.exception.NotFoundException;
import com.ktpt.quna.domain.model.Answer;
import com.ktpt.quna.domain.model.AnswerRepository;

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

    @Transactional
    public AnswerResponse update(Long id, AnswerRequest request) {
        Answer answer = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 Answer, id = " + id));
        answer.update(request.getContents());

        return AnswerResponse.from(answer);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
