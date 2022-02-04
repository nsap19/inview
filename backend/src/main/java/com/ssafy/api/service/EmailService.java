package com.ssafy.api.service;

import java.util.Random;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;

public interface EmailService {
    public static final String ePw = createKey();
	
    MimeMessage createMessage(String to) throws Exception;
    static String createKey() {
    	StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
	}
    void sendSimpleMessage(String to, String content) throws Exception;
    String createCode(String ePw);
}