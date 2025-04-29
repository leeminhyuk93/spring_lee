package com.mysite.lmh;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.mysite.lmh.Service.QuestionService;
import com.mysite.lmh.Service.UserService;
import com.mysite.lmh.entity.Question;
import com.mysite.lmh.entity.SiteUser;
import com.mysite.lmh.repository.QuestionRepository;
import com.mysite.lmh.repository.UserRepository;

@SpringBootTest
@Rollback(false)
class LmhApplicationTests {
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Test
	void test() {
		
		Optional<SiteUser> _user = this.userRepository.findById(10L);
		SiteUser user = _user.get();
		
		List<Question> questionList = this.questionService.getList();
		for (Question q : questionList) {
			q.setAuthor(user);
		}
		
		this.questionRepository.saveAll(questionList);
	}

}
