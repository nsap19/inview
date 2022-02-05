package com.ssafy.api.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
//import com.ssafy.groupcall.CallHandler;
//import com.ssafy.groupcall.Room;
//import com.ssafy.groupcall.UserSession;
import com.ssafy.api.service.VideoChatService;

/**
 * 화상 회의 관련 WebSocket 요청 처리를 위한 컨트롤러 정의
 */

@Controller
public class VideoChattingController {
	@Autowired
	VideoChatService videoChatService;
	
	private static final Logger log = LoggerFactory.getLogger(VideoChattingController.class);
	private static final Gson gson = new GsonBuilder().create();

	@MessageMapping("/videochat/recorded")
	public void recorded() {
		videoChatService.saveVideoChat();
	}
	
}
