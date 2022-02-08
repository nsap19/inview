package com.ssafy.common.util;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompInterceptor implements ChannelInterceptor {
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
//		  System.out.println("Command : " + headerAccessor.getCommand());
//        System.out.println("message : " + message);
//        System.out.println("헤더 : " + message.getHeaders());
		if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
//        	System.out.println("토큰1 : " + headerAccessor.getNativeHeader("Authorization").toString());
//        	System.out.println("토큰2 : " + Objects.requireNonNull(headerAccessor.getFirstNativeHeader("Authorization")).substring(7));
			jwtTokenUtil.handleError(
					Objects.requireNonNull(headerAccessor.getFirstNativeHeader("Authorization")).substring(7));
		}

		return message;
	}

	@Override
	public void postSend(Message message, MessageChannel channel, boolean sent) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		String sessionId = accessor.getSessionId();
		switch (accessor.getCommand()) {
		case CONNECT:
			// 유저가 Websocket으로 connect()를 한 뒤 호출됨
			break;
		case DISCONNECT:
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
			System.out.println(message.getPayload());
			System.out.println(accessor.getDetailedLogMessage(message.getPayload()));
			break;
		default:
			break;
		}
	}
}
