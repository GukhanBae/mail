package com.itbank.service;

import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class MailService {

	public String getAuthNumber() {
		Random ran = new Random();
		String authNumber = "";
		
		for(int i = 0; i < 6; i++) {
			authNumber += ran.nextInt(9);	// 0부터 9까지의 정수를 랜덤하게 뽑아서 authNumber에 6번 기록
		}
		return authNumber;
	}

	public String sendMail(String mailAddress, String authNumber, String account) {
		
		// 메일을 발송하는데에 사용하는 코드
		String host = "smtp.naver.com";		// Simple Mail Transfer Protocol
		int port = 465;
		final String username = account.split("/")[0];
		final String password = account.split("/")[1];
		
		String subject = "[KGITBANK] 인증번호입니다";
		String body = String.format("인증번호는 [%s] 입니다\n\n", authNumber);
		
		// 메일을 발송하는 서버에 대한 인증과 속성을 설정하기
		Properties props = System.getProperties();
		
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.trust", host);
		
		Session mailSession = Session.getDefaultInstance(props, new Authenticator() {
			String un = username;
			String pw = password;
			
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(un, pw);
			}
		});
		mailSession.setDebug(true); 	// 메일을 보내는 과정 중의 디버깅을 콘솔에 출력한다
		
		// 보내는 메일 내용 구성하기
		Message mimeMessage = new MimeMessage(mailSession);
		
		try {
			// 보내는 사람 주소
			mimeMessage.setFrom(new InternetAddress(username + "@naver.com"));
			
			// 받는 사람 주소
			mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(mailAddress));
			
			mimeMessage.setSubject(subject);	// 제목
			mimeMessage.setText(body);			// 내용
			
			Transport.send(mimeMessage); 		// 메일 발송 !!
			
		} catch(MessagingException e) {
			return "주소가 잘못되었습니다";
		}
		
		return authNumber;
	}

}

















