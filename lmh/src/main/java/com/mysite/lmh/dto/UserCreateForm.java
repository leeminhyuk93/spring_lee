package com.mysite.lmh.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserCreateForm {

	@NotEmpty(message = "")
	@Size(min = 8, max = 20, message = "아이디는 최소 8자, 최대 20자 사이로 입력해주세요.")
	private String username;
	
	@NotEmpty(message = "")
	@Size(min = 8, message = "비밀번호는 최소 8자 이상입니다.")
	private String password1;
	
	@NotEmpty(message = "")
	@Size(min = 8, message = "")
	private String password2;
	
	@NotBlank(message = "필수 항목입니다.")
	@Email
	private String email;
}
