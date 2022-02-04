package com.ssafy.api.service;

import java.util.Random;

import javax.mail.internet.MimeMessage;

public interface EmailService {
    public static final String ePw = createKey();
    public static String createKey() {
    	StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
	}
    MimeMessage createMessage(String to) throws Exception;
    void sendSimpleMessage(String to, String content) throws Exception;
}