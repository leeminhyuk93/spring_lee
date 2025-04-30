package com.mysite.lmh;

import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public String handleMethodNotAllowed(HttpServletRequest request, Exception ex, Model model) {
	    model.addAttribute("errorMessage", "잘못된 요청입니다. 페이지를 다시 확인해 주세요.");
	    model.addAttribute("exceptionDetail", ex.getMessage());  // 예외 메시지 출력
	    return "error/customError";
	}

}
