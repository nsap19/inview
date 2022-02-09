package com.ssafy.common.exception.handler;

public class NotExistsUserException extends RuntimeException {
	public NotExistsUserException() {
		super("존재하지 않는 유저입니다.");
	}
}
