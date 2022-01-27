package com.ssafy.common.exception.handler;

public class AlreadyFullParticipantException extends RuntimeException {
	public AlreadyFullParticipantException() {
		super("이미 최대 참가자가 참여한 미팅입니다.");
	}
}