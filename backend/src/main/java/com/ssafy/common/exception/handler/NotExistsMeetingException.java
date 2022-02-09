package com.ssafy.common.exception.handler;

public class NotExistsMeetingException extends RuntimeException {
	public NotExistsMeetingException() {
		super("존재하지 않는 미팅입니다.");
	}
}