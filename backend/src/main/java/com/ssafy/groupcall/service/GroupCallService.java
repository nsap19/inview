package com.ssafy.groupcall.service;

import org.springframework.web.socket.WebSocketSession;

import com.google.gson.JsonObject;
import com.ssafy.groupcall.UserSession;

public interface GroupCallService {
	public void start(UserSession userSession, final WebSocketSession session, JsonObject jsonMessage);
	public void play(UserSession userSession, final WebSocketSession session, JsonObject jsonMessage);
}
