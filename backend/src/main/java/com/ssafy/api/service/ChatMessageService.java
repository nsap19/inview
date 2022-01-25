package com.ssafy.api.service;

import com.ssafy.db.entity.ChatMessage;

public interface ChatMessageService {
	void saveChatMessage(ChatMessage message, String ope);
}
