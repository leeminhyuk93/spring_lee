package com.mysite.lmh.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class QuestionSequence {

	@Id
	private String seqDate; // yyyyMMdd, 자동으로 생성되지 않으므로 @Id 옵션 생략.
	
	private int lastNumber = 0;
}
