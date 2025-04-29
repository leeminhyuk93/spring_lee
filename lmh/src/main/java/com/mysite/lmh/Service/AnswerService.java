package com.mysite.lmh.Service;

import org.springframework.stereotype.Service;

import com.mysite.lmh.entity.Answer;
import com.mysite.lmh.entity.Question;
import com.mysite.lmh.entity.SiteUser;
import com.mysite.lmh.repository.AnswerRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnswerService {
	
	private final AnswerRepository answerRepository;

	@Transactional
	public Answer create(Question question, String content, SiteUser author) {
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setQuestion(question);
		answer.setAuthor(author);
		return this.answerRepository.save(answer);
	}
}
