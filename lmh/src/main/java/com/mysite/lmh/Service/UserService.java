package com.mysite.lmh.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mysite.lmh.Exceptions.DuplicateUsernameException;
import com.mysite.lmh.entity.SiteUser;
import com.mysite.lmh.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
//	@Transactional
//	public SiteUser create(String username, String password) {
//		if (this.userRepository.findByUsernamae().isPresent()) {
//			throw new DuplicateUsernameException("이미 사용 중인 아이디입니다.");
//		}
//	}
	
}
