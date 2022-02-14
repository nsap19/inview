package com.ssafy.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ChatMessage {
	private String meetingId;
	private String sessionId;
	private String date;
	private String time;
	private String message;
	private String sender; // 보내는 사람
	private String receiver; // 받는 사람, "" => 모두에게 로 기록한다.
	private CommandType command; // 받는 사람, "" => 모두에게 로 기록한다.

	@Builder
	public ChatMessage(String meetingId, String sessionId, String date, String time, String message, String sender,
			String receiver, CommandType command) {
		this.meetingId = meetingId;
		this.sessionId = sessionId;
		this.date = date;
		this.time = time;
		this.message = message;
		this.sender = sender;
		this.receiver = receiver;
		this.command = command;
	}

	public enum CommandType {
		READY, UNREADY, START, END, CONNECT, DISCONNECT, PARTICIPANT, HOST
	}
}
