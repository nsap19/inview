package com.ssafy.common.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ssafy.common.util.bean.ChattingParticipant;
import com.ssafy.common.util.bean.ChattingUser;
import com.ssafy.db.entity.User;

@Component
public class MeetingParticipant {
	ChattingUser chattingUser;
	
	public MeetingParticipant(ChattingUser chattingUser) {
		this.chattingUser = chattingUser;
	}

	public List<User> getParticipantByMeetingId(String meetingId) {
		Map<String, ChattingParticipant> hashMap = chattingUser.getParticipantByMeetingId().getOrDefault(meetingId,
				new HashMap<>());
		List<User> participantList = new LinkedList<>();
		for (String sessionId : hashMap.keySet()) {
			System.out.println(sessionId);
			participantList.add(hashMap.get(sessionId).getUser());
		}
		return participantList;
	}

	public boolean checkParticipant(String meetingId, String email) {
		System.out.println("checkParticipant / chattingUser : " + chattingUser);
		Map<String, ChattingParticipant> hashMap = chattingUser.getParticipantByMeetingId().getOrDefault(meetingId,
				new HashMap<>());
		List<User> participantList = new LinkedList<>();
		for (String sessionId : hashMap.keySet()) {
			if (hashMap.get(sessionId).getUser().getEmail().equals(email))
				return false;
		}
		return true;
	}

	public void addParticipantBySessionId(String sessionId, String meetingId, User user) {
		Map<String, ChattingParticipant> hashMap = chattingUser.getParticipantByMeetingId().getOrDefault(meetingId,
				new HashMap<>());
		ChattingParticipant participant = new ChattingParticipant(sessionId, meetingId, user);
		hashMap.put(sessionId, participant);
		chattingUser.getParticipantByMeetingId().put(meetingId, hashMap);
		chattingUser.getParticipantBySessionId().put(sessionId, participant);
	}

	public void deleteParticipantBySessionId(String sessionId) {
		ChattingParticipant participant = chattingUser.getParticipantBySessionId().get(sessionId);
		if (participant == null) {
			return;
		}
		String meetingId = participant.getMeetingId();
		Map<String, ChattingParticipant> hashMap = chattingUser.getParticipantByMeetingId().getOrDefault(meetingId,
				new HashMap<>());
		hashMap.remove(sessionId);
		if (hashMap.isEmpty())
			chattingUser.getParticipantByMeetingId().remove(meetingId);
		chattingUser.getParticipantBySessionId().remove(sessionId);
	}

}
