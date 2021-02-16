package com.ktpt.quna.infra;

import static com.ktpt.quna.domain.model.QMember.*;
import static com.ktpt.quna.domain.model.QQuestion.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ktpt.quna.domain.model.Question;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class QuestionQueryDAO {

	private final JPAQueryFactory query;

	public QuestionQueryDAO(JPAQueryFactory query) {
		this.query = query;
	}

	public List<Question> findAllWithMembers() {
		return query
			.selectFrom(question)
			.innerJoin(question.author, member).fetchJoin()
			.fetch();
	}
}
