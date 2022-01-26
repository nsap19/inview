package com.ssafy.common.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.ssafy.db.entity.User;

public class CurParticipant {
	private static HashMap<String, List<User>> participant = new HashMap<>();

	public List<User> getParticipantList(String meetingId) {
		return participant.getOrDefault(meetingId, new LinkedList<>());
	}

	public void setParticipantList(String meetingId, List<User> list) {
		participant.put(meetingId, list);
	}
}
