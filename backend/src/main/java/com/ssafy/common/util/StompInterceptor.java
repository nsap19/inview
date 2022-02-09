package com.ssafy.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompInterceptor implements ChannelInterceptor {
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private MeetingParticipant meetingParticipant;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
		String sessionId = headerAccessor.getSessionId();
		if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
			MessageHeaders headers = message.getHeaders();
			MultiValueMap<String, String> multiValueMap = headers.get(StompHeaderAccessor.NATIVE_HEADERS,
					MultiValueMap.class);
			HashMap<String, String> hashMap = new HashMap<>();
			String match = "[\\[\\],]";

			for (Entry<String, List<String>> head : multiValueMap.entrySet()) {
				hashMap.put(head.getKey(), String.valueOf(head.getValue()).replaceAll(match, ""));
			}

			System.out.println(hashMap.get("Authorization"));
			String token = Objects.requireNonNull(hashMap.get("Authorization"));
			jwtTokenUtil.handleError(token);
			String email = Objects.requireNonNull(jwtTokenUtil.getUserEmailFromJwt(token));
			String meetingId = hashMap.get("meetingId");

			if (!meetingParticipant.checkParticipant(meetingId, email)) {
				// 프론트와 처리 논의하기
				System.out.println("중복입장 에러 발생!!!");
			}

			meetingParticipant.addParticipantBySessionId(sessionId, meetingId, email);
		}

		return message;
	}

	@Override
	public void postSend(Message message, MessageChannel channel, boolean sent) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
		String sessionId = headerAccessor.getSessionId();
		switch (headerAccessor.getCommand()) {
		case CONNECT:
			// 유저가 Websocket으로 connect()를 한 뒤 호출됨
			// 인증 토큰으로 유저와 세션아이디 추가
			System.out.println(headerAccessor.getCommand());
			break;
		case DISCONNECT:
			// 세션아이디 기준으로 미팅방에서 유저 삭제
			System.out.println(headerAccessor.getCommand());
			meetingParticipant.deleteParticipantBySessionId(sessionId);
			// 유저가 Websocket으로 disconnect() 를 한 뒤 호출됨 or 세션이 끊어졌을 때 발생함(페이지 이동~ 브라우저 닫기 등)
			break;
		case SUBSCRIBE:
			// 유저가 Websocket으로 subscribe() 를 한 뒤 호출됨
			break;
		case UNSUBSCRIBE:
			// 유저가 Websocket으로 UNSUBSCRIBE() 를 한 뒤 호출됨
			break;
		case SEND:
			// 유저가 Websocket으로 SEND() 를 한 뒤 호출됨
			System.out.println("seesiongId : " + headerAccessor.getSessionId());
			System.out.println(headerAccessor.getDetailedLogMessage(message.getPayload()));
			break;
		default:
			break;
		}
	}
}
