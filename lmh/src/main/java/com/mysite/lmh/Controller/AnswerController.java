package com.mysite.lmh.Controller;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mysite.lmh.Service.AnswerService;
import com.mysite.lmh.Service.QuestionService;
import com.mysite.lmh.Service.UserService;
import com.mysite.lmh.dto.AnswerCreateForm;
import com.mysite.lmh.entity.Answer;
import com.mysite.lmh.entity.Question;
import com.mysite.lmh.entity.SiteUser;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RequestMapping("/answer")
@Controller
public class AnswerController {
	
	private final QuestionService questionService;
	private final UserService userService;
	private final AnswerService answerService;

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{id}")
	public String createAnswer(@PathVariable(value = "id") Long id, @Valid AnswerCreateForm answerCreateForm, 
			BindingResult bindingResult, Principal principal, Model model, RedirectAttributes redirectAttribute) {
		
		Question q = this.questionService.getQuestion(id);
		SiteUser u = this.userService.getUserByUsername(principal.getName())
				.orElse(null);
		
		if (bindingResult.hasErrors() || u == null) {
			model.addAttribute("question", q);
			System.out.println("에러발생: " + bindingResult.getAllErrors());
			return "board/questionDetail";
		}
		
		Answer a = this.answerService.create(q, answerCreateForm.getContent(), u);
		redirectAttribute.addFlashAttribute("messageIcon", "success");
		redirectAttribute.addFlashAttribute("message", "답변이 등록되었습니다.");
		return String.format("redirect:/question/detail/%s", q.getId());
	}
	
}
