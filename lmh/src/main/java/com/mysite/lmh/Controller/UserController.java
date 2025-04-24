package com.mysite.lmh.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysite.lmh.dto.UserCreateForm;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

	@GetMapping("/signup")
	public String signup(UserCreateForm userCreateForm) {
		return "user/signupForm";
	}
	
	@PostMapping("/signup")
	public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
		
		
		return "redirect:/question/list";
	}
	
}
