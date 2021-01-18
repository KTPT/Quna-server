package com.ktpt.quna.application.exception;

public class InvalidTokenException extends IllegalArgumentException {
	public InvalidTokenException(String s) {
		super(s);
	}
}