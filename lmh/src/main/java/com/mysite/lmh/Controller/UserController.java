package com.mysite.lmh.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mysite.lmh.Exceptions.DuplicateEmailException;
import com.mysite.lmh.Exceptions.DuplicateUsernameException;
import com.mysite.lmh.Service.UserService;
import com.mysite.lmh.dto.UserCreateForm;
import com.mysite.lmh.dto.UserCreateRequest;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;

	@GetMapping("/signup")
	public String signup(UserCreateForm userCreateForm) {
		return "user/signupForm";
	}
	
	@PostMapping("/signup")
	public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult,
			Model model, RedirectAttributes redirectAttributes) {
		try {
			UserCreateRequest request = UserCreateRequest.builder()
					.username(userCreateForm.getUsername())
					.password(userCreateForm.getPassword1())
					.email(userCreateForm.getEmail())
					.build();
			
			if (this.userService.getUserByUsername(request.getUsername()).isPresent()) {
				bindingResult.rejectValue("username", "duplicateUsername","* 현재 사용중인 아이디입니다.");
			}
			
			if (userCreateForm.getUsername().trim().isEmpty()) {
				bindingResult.rejectValue("username", "NotBlankUsername","* 아이디 입력은 필수입니다.");
			}
			
			if (this.userService.getUserByEmail(request.getEmail()).isPresent()) {
				bindingResult.rejectValue("email", "duplicateEmail","* 현재 사용중인 이메일입니다.");
			}
			
			if (userCreateForm.getPassword1().trim().isEmpty()) {
				bindingResult.rejectValue("password1", "NotBlankPassword1","* 비밀번호 입력은 필수입니다.");
				
			} else if (userCreateForm.getPassword2().trim().isEmpty()) {
				bindingResult.rejectValue("password2", "NotBlankPassword2","* 비밀번호 입력 확인은 필수입니다.");
				
			} else if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
				bindingResult.reject("NotEqualsPassword", "비밀번호 확인이 동일하지 않습니다.");
			}
			
			if (bindingResult.hasErrors()) {
				return "user/signupForm";
			}
			
			this.userService.create(request);
			return "redirect:/question/list";
			
		} catch (Exception e) {
			
			model.addAttribute("errorMessage", e.getMessage());
			return "user/signupForm";
		}
	}
	
}
