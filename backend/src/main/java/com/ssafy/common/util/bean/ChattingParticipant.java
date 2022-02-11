package com.ssafy.common.util.bean;

import com.ssafy.db.entity.User;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChattingParticipant {
	String sessionId;
	String meetingId;
	String ready;
	User user;
	
	@Builder
	public ChattingParticipant(String sessionId, String meetingId, String ready, User user) {
		this.sessionId = sessionId;
		this.meetingId = meetingId;
		this.ready = ready;
		this.user = user;
	}
}
