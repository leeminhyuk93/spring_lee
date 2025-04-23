package com.mysite.lmh.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QuestionForm {
	
	@NotBlank(message = "")
	@Size(min = 5, message = "최소 5글자 이상 입력해주세요.")
	private String subject;
	
	@NotBlank(message = "")
	@Size(min = 5, max = 200, message = "최소 5글자, 최대 200글자 입력해주세요.")
	private String content;
}
