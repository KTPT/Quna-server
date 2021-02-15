package com.ktpt.quna.domain.model;

import static com.ktpt.quna.domain.model.QMember.*;
import static com.ktpt.quna.domain.model.QQuestion.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class QuestionQueryDao {

	private final JPAQueryFactory query;

	public QuestionQueryDao(JPAQueryFactory query) {
		this.query = query;
	}

	public List<Question> findAllWithMembers() {
		return query
			.selectFrom(question)
			.innerJoin(question.author, member).fetchJoin()
			.fetch();
	}
}
