package com.ssafy.db.entity;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
	public ChatMessage(String meetingId, String message, String sender, CommandType command) {
		this.meetingId = meetingId;
		this.message = message;
		this.sender = sender;
		this.command = command;
	}

	public enum CommandType {
		READY, UNREADY, START, END, CONNECT, DISCONNECT, PARTICIPANT
	}
}
