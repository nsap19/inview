package com.ssafy.common.exception.handler;

public class NotParticipantException extends RuntimeException {
	public NotParticipantException() {
		super("참가하지 않은 유저입니다.");
	}
}