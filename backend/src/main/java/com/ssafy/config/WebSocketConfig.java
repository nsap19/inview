package com.ssafy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.ssafy.groupcall.CallHandler;
import com.ssafy.common.util.StompInterceptor;

@Configuration
@EnableWebSocketMessageBroker
@EnableWebSocket
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer, WebSocketConfigurer {

	@Bean
	public CallHandler groupCallHandler() {
		return new CallHandler();
	}

	@Bean
	public StompInterceptor stompInterceptor() {
		return new StompInterceptor();
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/stomp-chat")
				.setAllowedOrigins("http://i6a201.p.ssafy.io", "http://i6a201.p.ssafy.io:8080",
						"http://i6a201.p.ssafy.io:8080/api", "https://i6a201.p.ssafy.io",
						"https://i6a201.p.ssafy.io:8443", "https://i6a201.p.ssafy.io:8443/api",
						"https://localhost:8443", "http://localhost:3000")
				.withSockJS()
				.setClientLibraryUrl("https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.5/sockjs.min.js");
		;
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/publish");
		registry.enableSimpleBroker("/subscribe");
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(groupCallHandler(), "/groupcall")
				.setAllowedOrigins("http://i6a201.p.ssafy.io", "http://i6a201.p.ssafy.io:8080",
						"http://i6a201.p.ssafy.io:8080/api", "https://i6a201.p.ssafy.io",
						"https://i6a201.p.ssafy.io:8443", "https://i6a201.p.ssafy.io:8443/api",
						"https://localhost:8443", "http://localhost:3000")
				.withSockJS()
				.setClientLibraryUrl("https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.5/sockjs.min.js");
		;

	}

	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(stompInterceptor());
	}
}
