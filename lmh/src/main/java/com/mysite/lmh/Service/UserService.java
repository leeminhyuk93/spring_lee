package com.mysite.lmh.Service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mysite.lmh.Exceptions.DuplicateEmailException;
import com.mysite.lmh.Exceptions.DuplicateUsernameException;
import com.mysite.lmh.dto.UserCreateRequest;
import com.mysite.lmh.entity.SiteUser;
import com.mysite.lmh.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Transactional
	public SiteUser create(UserCreateRequest request) {
		if (this.userRepository.findByUsername(request.getUsername()).isPresent()) {
			throw new DuplicateUsernameException("이미 사용 중인 아이디입니다.");
			
		} else if (this.userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new DuplicateEmailException("이미 사용 중인 이메일입니다.");
		}
		
		SiteUser user = new SiteUser();
		user.setUsername(request.getUsername());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setEmail(request.getEmail());
		return this.userRepository.save(user);

	}
	
	public Optional<SiteUser> getUserByUsername(String username) {
		return this.userRepository.findByUsername(username);
	}
	
	public Optional<SiteUser> getUserByEmail(String email) {
		return this.userRepository.findByEmail(email);
	}
	
}
