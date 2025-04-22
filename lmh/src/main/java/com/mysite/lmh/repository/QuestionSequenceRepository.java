package com.mysite.lmh.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mysite.lmh.entity.QuestionSequence;

import jakarta.persistence.LockModeType;

public interface QuestionSequenceRepository extends JpaRepository<QuestionSequence, String> {
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT sq FROM QuestionSequence sq WHERE sq.seqDate = :seqDate")
	Optional<QuestionSequence> findBySeqDateForUpdate(@Param("seqDate") String seqDate);
}
