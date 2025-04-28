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
					SavedRequest savedRequest = (SavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
					if (savedRequest != null) {
						String redirectUrl = savedRequest.getRedirectUrl();
						response.sendRedirect(redirectUrl);
					} else {
						response.sendRedirect("/");
					}
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
