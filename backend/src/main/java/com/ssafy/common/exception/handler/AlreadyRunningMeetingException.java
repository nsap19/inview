package com.ssafy.common.exception.handler;

public class AlreadyRunningMeetingException extends RuntimeException {
	public AlreadyRunningMeetingException() {
		super("이미 시작된 미팅입니다.");
	}
}