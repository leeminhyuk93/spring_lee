package com.mysite.lmh.Exceptions;

public class DuplicateEmailException extends RuntimeException {

	public DuplicateEmailException(String message) {
		super(message);
	}
}
