package com.ssafy.common.exception.handler;

public class NotEqualPasswordException extends RuntimeException {
	public NotEqualPasswordException() {
		super("비밀번호가 일치하지 않습니다.");
	}
}