package com.ssafy.groupcall;

import java.io.IOException;

import org.kurento.client.IceCandidate;
import org.kurento.client.MediaPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ssafy.common.util.MeetingVerify;
import com.ssafy.db.entity.User;
import com.ssafy.groupcall.service.GroupCallService;

public class CallHandler extends TextWebSocketHandler {

	private static final Logger log = LoggerFactory.getLogger(CallHandler.class);

	private static final Gson gson = new GsonBuilder().create();

	@Autowired
	private RoomManager roomManager;

	@Autowired
	private UserRegistry registry;

	@Autowired
	private GroupCallService groupCallService;
	
	@Autowired
	private MeetingVerify meetingVerify;
	
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		final JsonObject jsonMessage = gson.fromJson(message.getPayload(), JsonObject.class);
		final UserSession user = registry.getBySession(session);
		
		if (user != null) {
			log.debug("Incoming message from user '{}': {}", user.getUserId(), jsonMessage);
		} else {
			log.debug("Incoming message from new user: {}", jsonMessage);
		}

		switch (jsonMessage.get("id").getAsString()) {
		case "joinRoom":
			joinRoom(jsonMessage, session);
			break;
		case "receiveVideoFrom":
			final int senderId = jsonMessage.get("sender").getAsInt();
			final UserSession sender = registry.getByUserId(senderId);
			final String sdpOffer = jsonMessage.get("sdpOffer").getAsString();
			user.receiveVideoFrom(sender, sdpOffer);
			break;
		case "leaveRoom":
			leaveRoom(user);
			break;
		case "onIceCandidate":
			JsonObject candidate = jsonMessage.get("candidate").getAsJsonObject();

			if (user != null) {
				IceCandidate cand = new IceCandidate(candidate.get("candidate").getAsString(),
						candidate.get("sdpMid").getAsString(), candidate.get("sdpMLineIndex").getAsInt());
				if(jsonMessage.get("userId")!=null) user.addCandidate(cand, jsonMessage.get("userId").getAsInt());
				else user.addCandidate(cand, user.getUserId()); //녹화 기능에 필요
			}
			break;
		case "start":
			groupCallService.start(user, session, jsonMessage);
			break;
		case "stop":
			if (user != null)
				user.stop();
			break;
		case "play":
			groupCallService.play(user, session, jsonMessage);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		UserSession user = registry.removeBySession(session);
		roomManager.getRoom(user.getMeetingId()).leave(user);
	}

	private void joinRoom(JsonObject params, WebSocketSession session) throws IOException {
		final int meetingId = params.get("meetingId").getAsInt();
		String token = params.get("accessToken").getAsString();
		
		User userInfo = meetingVerify.verifyToken(token, meetingId); //토큰 검증 및 해당회의실 participant인지 확인
		
		final int userId = userInfo.getUserId();
		log.info("PARTICIPANT {}: trying to join room {}", userId, meetingId);

		Room room = roomManager.getRoom(meetingId);
		final UserSession user = room.join(userId, session);
		registry.register(user);
	}

	private void leaveRoom(UserSession user) throws IOException {
		final Room room = roomManager.getRoom(user.getMeetingId());
		room.leave(user);
		if (room.getParticipants().isEmpty()) {
			roomManager.removeRoom(room);
		}
	}

	public void sendPlayEnd(WebSocketSession session, MediaPipeline pipeline) {
		try {
			JsonObject response = new JsonObject();
			response.addProperty("id", "playEnd");
			session.sendMessage(new TextMessage(response.toString()));
		} catch (IOException e) {
			log.error("Error sending playEndOfStream message", e);
		}
		// Release pipeline
		pipeline.release();
	}
}
