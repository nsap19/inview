package com.ssafy.common.exception.handler;

public class NotValidMeetingStatusException extends RuntimeException {
	public NotValidMeetingStatusException() {
		super("현재 미팅 상태에서 유효한 명령이 아닙니다.");
	}
}
