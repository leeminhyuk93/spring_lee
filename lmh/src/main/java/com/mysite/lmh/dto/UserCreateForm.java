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
	@Size(min = 8, max = 20, message = "")
	private String username;
	
	@NotEmpty(message = "")
	@Size(min = 8, message = "")
	private String password1;
	
	@NotEmpty(message = "")
	@Size(min = 8, message = "")
	private String password2;
	
	@NotBlank(message = "필수 항목입니다.")
	@Email
	private String email;
}
