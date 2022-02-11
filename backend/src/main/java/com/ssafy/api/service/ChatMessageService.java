package com.ssafy.api.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.ssafy.db.entity.ChatMessage;

public interface ChatMessageService {
	void saveChatMessage(ChatMessage message, String ope);
	void sendCommandMessage(ChatMessage message, String sessionId);
}
