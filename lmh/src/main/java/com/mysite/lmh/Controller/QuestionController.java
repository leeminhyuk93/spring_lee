package com.mysite.lmh.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.lmh.entity.Question;

import com.mysite.lmh.Service.QuestionService;

@Controller
public class QuestionController {

	@Autowired
	private QuestionService questionService;
	
	@GetMapping("/questions")
	public String getQuestions(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
		int listNums = 10;
		int blockSize = 5;
		
		Page<Question> questionPage = this.questionService.getQuestions(page, listNums);
		long totalQuestion = questionPage.getTotalElements();
		int totalPages = questionPage.getTotalPages();
		
		// 현재 를록 번호
		int currentBlock = page / blockSize;
		
		// 시작 페이지와 끝 페이지
		int startPage = currentBlock * blockSize;
		int endPage = Math.min(startPage + blockSize, totalPages);
		
		model.addAttribute("questionList", questionPage.getContent()); // 현재 페이지의 데이터
		model.addAttribute("totalPages", totalPages); // 총 페이지 수
		model.addAttribute("totalQuestion", totalQuestion); // 전체 게시물 수
		model.addAttribute("currentPage", page); // 현재 페이지 번호
		model.addAttribute("listNums", listNums); // 현재 페이지 출력 수
		
		// 페이지 블록 관련
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("hasPrevBlock", startPage > 0);
		model.addAttribute("hasNextBlock", endPage < totalPages);
		
		return "board/questionList";
	}
}
