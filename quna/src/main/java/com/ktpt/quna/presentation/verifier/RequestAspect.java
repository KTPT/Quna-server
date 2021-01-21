package com.ktpt.quna.presentation.verifier;

import com.ktpt.quna.domain.model.QuestionRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class RequestAspect {
    private static final String QUESTION_ID = "questionId";

    private final QuestionRepository questionRepository;

    public RequestAspect(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Pointcut("@within(com.ktpt.quna.presentation.verifier.QuestionShouldExist)")
    public void questionShouldExist() {
    }

    @Before("questionShouldExist()")
    public void verifyQuestion(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        List<String> parameters = Arrays.asList(methodSignature.getParameterNames());

        try {
            Long questionId = (Long) joinPoint.getArgs()[parameters.indexOf(QUESTION_ID)];

            if (!questionRepository.existsById(questionId)) {
                throw new InvalidRequestException("존재하지 않는 question, id: " + questionId);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("메소드 파라미터에 questionId가 존재하지 않습니다.");
        }
    }
}
