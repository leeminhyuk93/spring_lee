package com.mysite.lmh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LmhApplication {

	public static void main(String[] args) {
		SpringApplication.run(LmhApplication.class, args);
	}
}
