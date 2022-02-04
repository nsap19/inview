package com.ssafy.api.service;

import java.util.Random;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {
	
	@Autowired
    private final JavaMailSender emailSender;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public static final String ePw = createKey();

    public MimeMessage createMessage(String to) throws Exception{ // 회원가입 시 인증 메일 전송
        logger.info("대상 : "+ to);
        logger.info("인증 번호 : " + ePw);
        MimeMessage  message = emailSender.createMimeMessage();

        String code = createCode(ePw);
        message.addRecipients(RecipientType.TO, to); //보내는 대상
        message.setSubject("확인 코드: " + code); //제목

        String msg="";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 인증 코드 란에 입력하세요.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += code;
        msg += "</td></tr></tbody></table></div>";

        message.setText(msg, "utf-8", "html"); //내용
        message.setFrom(new InternetAddress("ssafy6a201@gmail.com","inview")); //보내는 사람

        return message;
    }
    
    public MimeMessage createMessagePw(String to, String content) throws Exception { // 비밀번호찾기 시 임시 비밀번호 발급
    	logger.info("대상 : "+ to);
        logger.info("인증 번호 : " + ePw);
        MimeMessage  message = emailSender.createMimeMessage();

        String code = createCode(ePw);
        message.addRecipients(RecipientType.TO, to); //보내는 대상
        message.setSubject("확인 코드: " + code); //제목

        String msg="";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 인증 코드 란에 입력하세요.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += code;
        msg += "</td></tr></tbody></table></div>";

        message.setText(msg, "utf-8", "html"); //내용
        message.setFrom(new InternetAddress("ssafy6a201@gmail.com","inview")); //보내는 사람

        return message;
    }

    // 인증코드 만들기
    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
    }

    public void sendSimpleMessage(String to, String content) throws Exception {
    	MimeMessage message;
    	
    	if(content == "")
    		message = createMessagePw(to, content);
    	else
    		message = createMessage(to);
        try { //예외처리
            emailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
    }
    
    public String createCode(String ePw){
        return ePw.substring(0, 3) + ePw.substring(3, 6);
    }
}
