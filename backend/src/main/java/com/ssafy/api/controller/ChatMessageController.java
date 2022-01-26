package com.ssafy.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.ssafy.api.service.ChatMessageService;
import com.ssafy.db.entity.ChatMessage;

/**
 * 채팅 관련 stomp API 요청 처리를 위한 컨트롤러 정의.
 */
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
		String receiver = message.getReceiver() == "" ? message.getReceiver() : "/" + message.getReceiver();
		message.setMessage(message.getSender() + "님이 입장하셨습니다.");
		chatMessageService.saveChatMessage(message, "subscribe");
		this.template.convertAndSend("/subscribe/chat/room/" + message.getMeetingId() + receiver, message);
	}

	@MessageMapping("/chat/message")
	public void message(@Payload ChatMessage message) {
		String receiver = message.getReceiver() == "" ? message.getReceiver() : "/" + message.getReceiver();
		chatMessageService.saveChatMessage(message, "send");
		this.template.convertAndSend("/subscribe/chat/room/" + message.getMeetingId() + receiver, message);
	}

	@MessageMapping("/chat/leave")
	public void leave(@Payload ChatMessage message) {
		String receiver = message.getReceiver() == "" ? message.getReceiver() : "/" + message.getReceiver();
		message.setMessage(message.getSender() + "님이 퇴장하셨습니다.");
		chatMessageService.saveChatMessage(message, "unsubscribe");
		this.template.convertAndSend("/subscribe/chat/room/" + message.getMeetingId() + receiver, message);
	}
}
