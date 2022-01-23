package com.ssafy.api.service;

import com.ssafy.db.entity.ChatMessage;

public interface ChatMessageService {
	void subscribeChatMessage(ChatMessage message);
	void unsubscribeChatMessage(ChatMessage message);
	void saveChatMessage(ChatMessage message);
}
