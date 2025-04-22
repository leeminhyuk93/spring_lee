package com.mysite.lmh;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.mysite.lmh.Service.QuestionService;

@SpringBootTest
@Rollback(false)
class LmhApplicationTests {
	
	@Autowired
	private QuestionService questionService;

	@Test
	void test() {
		
		for (int i = 1; i <= 100; i++) {
			String subject = String.format("테스트 게시물[%03d]", i);
			String content = "테스트 작성입니다.";
			questionService.create(subject, content);
		}
	}

}
