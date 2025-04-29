package com.mysite.lmh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.lmh.entity.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
