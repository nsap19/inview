package com.ssafy.common.util.bean;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.ssafy.db.entity.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Component
public class ChattingUser {
	HashMap<String, Map<String, ChattingParticipant>> participantByMeetingId;
	HashMap<String, ChattingParticipant> participantBySessionId;
	
	public ChattingUser(HashMap<String, Map<String, ChattingParticipant>> participantByMeetingId,
			HashMap<String, ChattingParticipant> participantBySessionId) {
		this.participantByMeetingId = participantByMeetingId;
		this.participantBySessionId = participantBySessionId;
	}
	
}
