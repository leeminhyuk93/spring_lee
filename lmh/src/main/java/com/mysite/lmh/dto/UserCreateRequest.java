package com.mysite.lmh.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCreateRequest {

	private String username;
	private String password;
	private String email;
}
