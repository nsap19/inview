package com.ssafy.db.entity;

import java.awt.TrayIcon.MessageType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private String meetingId;
    private String userName;
    private String message;
    private MessageType type;
}
