package com.ktpt.quna.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ktpt.quna.application.dto.QuestionRequest;
import com.ktpt.quna.application.dto.QuestionResponse;
import com.ktpt.quna.application.exception.NotFoundException;
import com.ktpt.quna.domain.model.Question;
import com.ktpt.quna.domain.model.QuestionQueryDao;
import com.ktpt.quna.domain.model.QuestionRepository;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionQueryDao questionQueryRepository;

    public QuestionService(QuestionRepository questionRepository, QuestionQueryDao questionQueryRepository) {
        this.questionRepository = questionRepository;
        this.questionQueryRepository = questionQueryRepository;
    }

    @Transactional
    public QuestionResponse create(QuestionRequest request, Long authorId) {
        Question question = request.toEntity(authorId);
        Question saved = questionRepository.save(question.create());

        return QuestionResponse.from(saved);
    }

    @Transactional
    public QuestionResponse update(Long id, QuestionRequest request) {
        Question question = questionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 Question, id = " + id));
        Question saved = questionRepository.save(
            question.update(request.getTitle(), request.getContents(), request.getResponderId()));

        return QuestionResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public QuestionResponse findById(Long id) {
        Question question = questionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 Question, id = " + id));

        return QuestionResponse.from(question);
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> findAll() {
        return QuestionResponse.listOf(questionQueryRepository.findAllWithMembers());
    }

    @Transactional
    public void delete(Long id) {
        questionRepository.deleteById(id);
    }
}
