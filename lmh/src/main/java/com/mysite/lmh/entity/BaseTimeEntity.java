package com.mysite.lmh.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass // @Entity 클래스가 상속받을 수 있도록 함
@EntityListeners(AuditingEntityListener.class) // 엔티티의 생명주기 이벤트를 감지하고, 특정 작업을 자동으로 처리할 수 있게 해줍니다.
public abstract class BaseTimeEntity { // 이 클래스는 추상화(Abstract) 클래스입니다.
	
	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createDate;
	
	@LastModifiedDate
	@Column(updatable = false)
	private LocalDateTime lastModifiedDate;
}
