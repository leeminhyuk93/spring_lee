package com.mysite.lmh.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginForm {

	@NotBlank(message = "")
	private String username;
	
	@NotBlank(message = "")
	private String password;
}
