package com.ssafy.common.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ssafy.api.service.UserService;
import com.ssafy.db.entity.User;

import lombok.Getter;

@Component
public class MeetingParticipant {
	
	private static UserService userService;
	private static HashMap<String, Map<String, ParticipantInfo>> participantByMeetingId;
	private static HashMap<String, ParticipantInfo> participantBySessionId;

	@Getter
	static class ParticipantInfo {
		String sessionId;
		String meetingId;
		User user;

		public ParticipantInfo(String sessionId, String meetingId, User user) {
			this.sessionId = sessionId;
			this.meetingId = meetingId;
			this.user = user;
		}
	}

	@Autowired
	public MeetingParticipant(UserService userService, HashMap<String, Map<String, ParticipantInfo>> participantByMeetingId,
			HashMap<String, ParticipantInfo> participantBySessionId) {
		this.userService = userService;
		this.participantByMeetingId = participantByMeetingId;
		this.participantBySessionId = participantBySessionId;
	}

	public static List<User> getParticipantByMeetingId(String meetingId) {
		Map<String, ParticipantInfo> hashMap = participantByMeetingId.getOrDefault(meetingId, new HashMap<>());
		List<User> participantList = new LinkedList<>();
		for (String sessionId : hashMap.keySet()) {
			participantList.add(hashMap.get(sessionId).getUser());
		}
		return participantList;
	}

	public static boolean checkParticipant(String meetingId, String email) {
		Map<String, ParticipantInfo> hashMap = participantByMeetingId.getOrDefault(meetingId, new HashMap<>());
		List<User> participantList = new LinkedList<>();
		for (String sessionId : hashMap.keySet()) {
			if (hashMap.get(sessionId).getUser().getEmail().equals(email))
				return false;
		}
		return true;
	}

	public static void addParticipantBySessionId(String sessionId, String meetingId, String email) {
		Map<String, ParticipantInfo> hashMap = participantByMeetingId.getOrDefault(meetingId, new HashMap<>());
		ParticipantInfo participant = new ParticipantInfo(sessionId, meetingId, userService.getUserByEmail(email));
		hashMap.put(sessionId, participant);
		participantByMeetingId.put(meetingId, hashMap);
		participantBySessionId.put(sessionId, participant);
	}

	public static void deleteParticipantBySessionId(String sessionId) {
		ParticipantInfo participant = participantBySessionId.get(sessionId);
		if (participant == null) {
			return;
		}
		String meetingId = participant.getMeetingId();
		Map<String, ParticipantInfo> hashMap = participantByMeetingId.getOrDefault(meetingId, new HashMap<>());
		hashMap.remove(sessionId);
		if (hashMap.isEmpty())
			participantByMeetingId.remove(meetingId);
		participantBySessionId.remove(sessionId);
	}

}
