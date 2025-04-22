package com.mysite.lmh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mysite.lmh.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}
