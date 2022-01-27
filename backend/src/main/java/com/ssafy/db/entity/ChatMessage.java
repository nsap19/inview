package com.ssafy.db.entity;

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
    private String date;
    private String time;
    private String message;
    private String sender; // 보내는 사람
    private String receiver; // 받는 사람, "" => 모두에게 로 기록한다.
}
