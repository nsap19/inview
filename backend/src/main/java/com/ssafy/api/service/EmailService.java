package com.ssafy.api.service;

import javax.mail.internet.MimeMessage;

public interface EmailService {
	String createKey() throws Exception;
    MimeMessage createMessage(String to, String code) throws Exception;
    MimeMessage createMessagePw(String to, String password) throws Exception;
    String sendSimpleMessage(String to, String content) throws Exception;
}