package com.ssafy.common.util.bean;

import com.ssafy.db.entity.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChattingParticipant {
	String sessionId;
	String meetingId;
	User user;

	public ChattingParticipant(String sessionId, String meetingId, User user) {
		this.sessionId = sessionId;
		this.meetingId = meetingId;
		this.user = user;
	}
}
