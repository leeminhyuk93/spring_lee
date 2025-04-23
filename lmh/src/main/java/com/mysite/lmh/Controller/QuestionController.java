package com.mysite.lmh.Controller;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.lmh.entity.Question;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import com.mysite.lmh.Service.QuestionService;
import com.mysite.lmh.dto.QuestionForm;

@Controller
@RequestMapping("/question")
public class QuestionController {

	@Autowired
	private QuestionService questionService;
	
	@GetMapping("/list")
	public String getQuestions(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
		int listNums = 10; // 한 번에 보여질 게시물 수
		int blockSize = 5; // 한 번에 보여질 하단의 페이지 수
		
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
	
	@GetMapping("/create")
	public String questionCreate(Model model, HttpSession session) {
		String token = UUID.randomUUID().toString();
		session.setAttribute("formToken", token);
		model.addAttribute("formToken", token);
		model.addAttribute("questionForm", new QuestionForm());
		return "board/questionForm";
	}
	
	@PostMapping("/create")
	public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, 
								 @RequestParam("formToken") String formToken, HttpSession session, Model model) {
		
		String sessionToken = (String) session.getAttribute("formToken");
		
		if (sessionToken == null || !sessionToken.equals(formToken)) {
			bindingResult.reject("duplicateSubmission", "중복 제출이 감지되었습니다.");
			return "board/questionForm";
		}
		
		session.removeAttribute("formToken");
		
		
		if (questionForm.getSubject() == null || questionForm.getSubject().trim().isEmpty()) {
			bindingResult.reject("subjectIsNothing", "제목이 입력되지 않았습니다.");
			
		} else if (questionForm.getContent() == null || questionForm.getContent().trim().isEmpty()) {
			bindingResult.reject("contentIsNothing", "내용이 입력되지 않았습니다.");
		}
		
		if (bindingResult.hasErrors()) {
			return "board/questionForm";
		}
			
		this.questionService.create(questionForm.getSubject(), questionForm.getContent());
		
		return "redirect:/question/list";
	}
}
