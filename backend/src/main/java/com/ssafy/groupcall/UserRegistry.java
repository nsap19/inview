package com.ssafy.groupcall;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.WebSocketSession;

/**
 * Map of users registered in the system. This class has a concurrent hash map
 * to store users, using its name as key in the map.
 * 
 */
public class UserRegistry {

	private final ConcurrentHashMap<Integer, UserSession> usersByUserId = new ConcurrentHashMap<>();
//	private final ConcurrentHashMap<String, UserSession> usersByName = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, UserSession> usersBySessionId = new ConcurrentHashMap<>();

	public void register(UserSession user) {
		usersByUserId.put(user.getUserId(), user);
		usersBySessionId.put(user.getSession().getId(), user);
	}

	public UserSession getByUserId(int userId) {
		return usersByUserId.get(userId);
	}

	public UserSession getBySession(WebSocketSession session) {
		return usersBySessionId.get(session.getId());
	}

	public boolean exists(int userId) {
		return usersByUserId.keySet().contains(userId);
	}

	public UserSession removeBySession(WebSocketSession session) {
		final UserSession user = getBySession(session);
		usersByUserId.remove(user.getUserId());
		usersBySessionId.remove(session.getId());
		return user;
	}

}