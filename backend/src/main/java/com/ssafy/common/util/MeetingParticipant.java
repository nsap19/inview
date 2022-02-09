package com.ssafy.common.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ssafy.api.service.UserService;
import com.ssafy.db.entity.Participant;
import com.ssafy.db.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Component
public class MeetingParticipant {
	private static UserService userService;
	private static HashMap<String, Map<String, Participant>> participantByMeetingId;
	private static HashMap<String, Participant> participantBySessionId;

	@Getter
	static class Participant {
		String sessionId;
		String meetingId;
		User user;

		public Participant(String sessionId, String meetingId, User user) {
			this.sessionId = sessionId;
			this.meetingId = meetingId;
			this.user = user;
		}
	}

	@Autowired
	public MeetingParticipant(UserService userService, HashMap<String, Map<String, Participant>> participantByMeetingId,
			HashMap<String, Participant> participantBySessionId) {
		this.userService = userService;
		this.participantByMeetingId = participantByMeetingId;
		this.participantBySessionId = participantBySessionId;
	}

	public static List<User> getParticipantByMeetingId(String meetingId) {
		Map<String, Participant> hashMap = participantByMeetingId.getOrDefault(meetingId, new HashMap<>());
		List<User> participantList = new LinkedList<>();
		for (String sessionId : hashMap.keySet()) {
			participantList.add(hashMap.get(sessionId).getUser());
		}
		return participantList;
	}

	public static boolean checkParticipant(String meetingId, String email) {
		Map<String, Participant> hashMap = participantByMeetingId.getOrDefault(meetingId, new HashMap<>());
		List<User> participantList = new LinkedList<>();
		for (String sessionId : hashMap.keySet()) {
			if (hashMap.get(sessionId).getUser().getEmail().equals(email))
				return false;
		}
		return true;
	}

	public static void addParticipantBySessionId(String sessionId, String meetingId, String email) {
		Map<String, Participant> hashMap = participantByMeetingId.getOrDefault(meetingId, new HashMap<>());
		Participant participant = new Participant(sessionId, meetingId, userService.getUserByEmail(email));
		hashMap.put(sessionId, participant);
		participantByMeetingId.put(meetingId, hashMap);
		participantBySessionId.put(sessionId, participant);
	}

	public static void deleteParticipantBySessionId(String sessionId) {
		Participant participant = participantBySessionId.get(sessionId);
		if (participant == null) {
			return;
		}
		String meetingId = participant.getMeetingId();
		Map<String, Participant> hashMap = participantByMeetingId.getOrDefault(meetingId, new HashMap<>());
		hashMap.remove(sessionId);
		if (hashMap.isEmpty())
			participantByMeetingId.remove(meetingId);
		participantBySessionId.remove(sessionId);
	}

}
