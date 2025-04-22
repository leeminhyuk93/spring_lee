package com.mysite.lmh.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mysite.lmh.entity.Question;
import com.mysite.lmh.entity.QuestionSequence;
import com.mysite.lmh.repository.QuestionRepository;
import com.mysite.lmh.repository.QuestionSequenceRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {

	private final QuestionRepository questionRepository;
	private final QuestionSequenceRepository sequenceRepository;
	
	@Transactional
	public Question create(String subject, String content) {
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		int nextSeq = getNextSequence(today);
		String questionNo = String.format("%s-%05d", today, nextSeq);
		
		Question question = new Question();
		question.setSubject(subject);
		question.setContent(content);
		question.setQuestionNo(questionNo);
		
		return questionRepository.save(question);
	}
	
	private int getNextSequence(String today) {
		QuestionSequence seq = sequenceRepository.findBySeqDateForUpdate(today) // 해당 일자에 해당하는 값을 검색하여 반환
				.orElseGet(() -> { // 존재하지 않으면 오늘 일자에 해당하는 객체(데이터)를 생성
					QuestionSequence newSeq = new QuestionSequence();
					newSeq.setSeqDate(today);
					newSeq.setLastNumber(0);
					return newSeq;
				});
		
		int newNumber = seq.getLastNumber() + 1;
		seq.setLastNumber(newNumber);
		this.sequenceRepository.save(seq);
		return newNumber;
	}
	
	public List<Question> getList() {
		return this.questionRepository.findAll();
	}
}
