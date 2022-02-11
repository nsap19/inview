package com.ssafy.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;

import com.ssafy.api.service.ChatMessageService;
import com.ssafy.common.util.JwtTokenUtil;
import com.ssafy.db.entity.ChatMessage;
import com.ssafy.db.entity.ChatMessage.CommandType;

/**
 * 채팅 관련 stomp API 요청 처리를 위한 컨트롤러 정의.
 */
@Controller
public class ChatMessageController {
	@Autowired
	private ChatMessageService chatMessageService;
	
	@MessageMapping("/chat/join")
	public void join(@Payload ChatMessage message, @Header("Authorization") String token) {
		message.setMessage(message.getSender() + "님이 입장하셨습니다.");
		chatMessageService.saveChatMessage(message, "subscribe");
		
	}

	@MessageMapping("/chat/message")
	public void message(@Payload ChatMessage message, @Header("Authorization") String token) {
		chatMessageService.saveChatMessage(message, "send");
	}

	@MessageMapping("/chat/leave")
	public void leave(@Payload ChatMessage message, @Header("Authorization") String token) {
		message.setMessage(message.getSender() + "님이 퇴장하셨습니다.");
		chatMessageService.saveChatMessage(message, "unsubscribe");
	}
	
	@MessageMapping("/chat/command")
	public void command(@Payload ChatMessage message, @Header("Authorization") String token) {
		CommandType commandType = message.getCommand();
		if(commandType == null) {
			return;
 		}
		switch (commandType) {
		case READY:
		case START:
			chatMessageService.sendCommandMessage(message);
			break;
		default:
			break;
		}
	}
}
