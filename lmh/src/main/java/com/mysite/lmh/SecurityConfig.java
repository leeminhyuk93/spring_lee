package com.mysite.lmh;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.csrf(csrf -> csrf.disable());
		http.authorizeHttpRequests((auth) -> auth
				.anyRequest().permitAll()
		);
		http.formLogin((formLogin) -> formLogin
				.loginPage("/user/login") // 로그인 POST 전송 URL
				.defaultSuccessUrl("/", false)
				.successHandler((request, response, authentication) -> {
					request.getSession().setAttribute("message", "로그인되었습니다!");
					request.getSession().setAttribute("messageIcon", "success");
					
					// 1순위: 로그인 요구로 인해 로그인폼으로 이동되었을 때 url 처리
					SavedRequest savedRequest = (SavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
					if (savedRequest != null) {
						String method = savedRequest.getMethod();
						if ("GET".equalsIgnoreCase(method)) {
							response.sendRedirect(savedRequest.getRedirectUrl());
						} else {
							response.sendRedirect("/");
						}
						return;
					}
					
					// 2순위: 로그인 페이지 수동으로 진입 시 전에 저장한 이전 url로 이동
					String prevPage = (String) request.getSession().getAttribute("prevPage");
					if (prevPage != null) {
						request.getSession().removeAttribute("prevPage");
						response.sendRedirect(prevPage);
						return;
					}
					
					// 3순위: 기본 URL
				    response.sendRedirect("/");
					
				})
		);
		http.logout((logout) -> logout
				.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
				.logoutSuccessUrl("/")
				.logoutSuccessHandler((request, response, authentication) -> {
					request.getSession().setAttribute("message", "로그아웃되었습니다.");
					request.getSession().setAttribute("messageIcon", "info");
					response.sendRedirect("/");
				})
				.invalidateHttpSession(true)
		);
		
		return http.build();
	}
	
	@Bean
	PasswordEncoder PasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
