package com.mysite.lmh.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AnswerCreateForm {

	@NotBlank(message = "")
	@Size(min = 10, max = 50, message = "내용은 10자 이상, 50자 이하로 작성해주세요.")
	private String content;
	
}
