package com.mysite.lmh.Controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mysite.lmh.entity.Question;
import com.mysite.lmh.Service.QuestionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MainController {
	
	private final QuestionService QuestionService;

	@GetMapping("/")
	public String root(Model model) {
		return "redirect:/questions";
	}
}

