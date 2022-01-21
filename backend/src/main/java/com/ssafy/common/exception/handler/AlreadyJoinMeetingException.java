package com.ssafy.common.exception.handler;

public class AlreadyJoinMeetingException extends RuntimeException {
	public AlreadyJoinMeetingException() {
		super("이미 참가한 유저입니다.");
	}
}