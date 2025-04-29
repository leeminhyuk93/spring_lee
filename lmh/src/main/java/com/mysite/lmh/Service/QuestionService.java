package com.mysite.lmh.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysite.lmh.Exceptions.UserNotFoundException;
import com.mysite.lmh.entity.Question;
import com.mysite.lmh.entity.QuestionSequence;
import com.mysite.lmh.entity.SiteUser;
import com.mysite.lmh.repository.QuestionRepository;
import com.mysite.lmh.repository.QuestionSequenceRepository;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {

	private final QuestionRepository questionRepository;
	private final QuestionSequenceRepository sequenceRepository;

	
	// 업데이트 메서드 테스트 미완료
	@Transactional
	public Question update(Long id, String subject, String content) {
		Optional<Question> _question = this.questionRepository.findById(id);
		if (_question.isEmpty()) {
			throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
		} else {
			Question question = _question.get();
			question.setSubject(subject);
			question.setContent(content);
			return this.questionRepository.save(question);
		}
	}
	
	@Transactional
	public Question create(String subject, String content, SiteUser author) {
		
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		int nextSeq = getNextSequence(today);
		String questionNo = String.format("%s-%05d", today, nextSeq);
		
		Question question = new Question();
		question.setSubject(subject);
		question.setContent(content);
		question.setAuthor(author);
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
	

	public Page<Question> getQuestions(int page, int size) {
		Sort sort = Sort.by("createDate").descending();
		Pageable pageable = PageRequest.of(page, size, sort);
		return this.questionRepository.findAll(pageable);
	}
	
	public Question getQuestion(Long id) {
		return this.questionRepository.findById(id)
				.orElse(null);
	}
}
