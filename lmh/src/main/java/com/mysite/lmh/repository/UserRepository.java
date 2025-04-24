package com.mysite.lmh.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mysite.lmh.entity.SiteUser;

public interface UserRepository extends JpaRepository<SiteUser, Long> {

	Optional<SiteUser> findByUsernamae();
}
