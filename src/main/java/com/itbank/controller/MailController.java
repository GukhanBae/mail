package com.itbank.controller;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.itbank.service.Hash;
import com.itbank.service.MailService;

@RestController
public class MailController {
	
	@Autowired private MailService mailService;
	
	@GetMapping(value="/mailto/{mailAddress}/", produces="text/plain;charset=utf-8")
	public String mailto(@PathVariable String mailAddress, HttpSession session) throws IOException {
		
		System.out.println("인증번호를 받을 이메일 주소 : " + mailAddress);
		
		String authNumber = mailService.getAuthNumber();
		String hashNumber = Hash.getHash(authNumber);
		session.setAttribute("hashNumber", hashNumber); 	// 발송할 인증번호의 해시값
		
		String account = null;
		String filePath = session.getServletContext().getRealPath("/WEB-INF/data/mailAccount.dat");
		File f = new File(filePath);
		if(f.exists() == false) {
			return "메일 전송에 필요한 계정 정보를 찾을 수 없습니다";
		}
		Scanner sc = new Scanner(f);
		while(sc.hasNextLine()) {
			account = sc.nextLine();
		}
		sc.close();
		
		String result = mailService.sendMail(mailAddress, authNumber, account);
		
		if(result.equals(authNumber)) {	// 정상적으로 메일을 발송했다면 인증번호가 반환되어서 브라우저에게는 hash값을 전달한다
			return hashNumber;
		}
		else {
			return result;				// 아니라면 상태를 알리는 문자열 메시지를 반환한다
		}
	}

	
	@GetMapping("/getAuthResult/{userNumber}")
	public boolean getAuthResult(@PathVariable String userNumber, HttpSession session) {
		// userNumber를 해시처리한 값과, 세션에 저장해둔 hashNumber를 비교해서 같으면 true를 반환하고 다르면 false를 반환
		return Hash.getHash(userNumber).equals((String)session.getAttribute("hashNumber"));
	}
}