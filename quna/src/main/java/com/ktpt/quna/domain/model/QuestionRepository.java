package com.ktpt.quna.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Long> {

	@Query("select q from Question q join fetch q.author")
	List<Question> findAllWithMembers();
}
