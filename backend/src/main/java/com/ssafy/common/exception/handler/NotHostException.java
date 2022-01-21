package com.ssafy.common.exception.handler;

public class NotHostException extends RuntimeException {
	public NotHostException() {
		super("호스트가 아닙니다.");
	}
}