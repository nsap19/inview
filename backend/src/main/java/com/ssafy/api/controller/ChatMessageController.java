package com.ssafy.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.ssafy.api.service.ChatMessageService;
import com.ssafy.db.entity.ChatMessage;

@Controller
public class ChatMessageController {
	@Autowired
	ChatMessageService chatMessageService;
	
	private final SimpMessagingTemplate template;

	@Autowired
	public ChatMessageController(SimpMessagingTemplate template) {
		this.template = template;
	}

	@MessageMapping("/chat/join")
	public void join(@Payload ChatMessage message) {
		message.setMessage(message.getUserName() + "님이 입장하셨습니다.");
		chatMessageService.subscribeChatMessage(message);
		this.template.convertAndSend("/subscribe/chat/room/" + message.getMeetingId(), message);
	}

	@MessageMapping("/chat/message")
	public void message(@Payload ChatMessage message) {
		chatMessageService.saveChatMessage(message);
		this.template.convertAndSend("/subscribe/chat/room/" + message.getMeetingId(), message);
	}
	
	@MessageMapping("/chat/leave")
	public void leave(@Payload ChatMessage message) {
		message.setMessage(message.getUserName() + "님이 퇴장하셨습니다.");
		chatMessageService.unsubscribeChatMessage(message);
		this.template.convertAndSend("/subscribe/chat/room/" + message.getMeetingId(), message);
	}
}
