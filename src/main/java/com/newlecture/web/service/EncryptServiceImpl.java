package com.newlecture.web.service;

import java.security.MessageDigest;
import java.security.SecureRandom;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EncryptServiceImpl implements EncryptService{
	
	SecureRandom sr = new SecureRandom();
	
	@Override
	public String getSalt() {
		byte[] salt = new byte[20];
		
		// 난수 생성하여 인자로 들어온 곳에다가 넣어줌
		sr.nextBytes(salt);
		
		StringBuffer sb = new StringBuffer();
		for (byte b : salt) {
			// 02x는 byte -> 10진수
			sb.append(String.format("%02x", b));
		}
		
		return sb.toString();
	}

	@Override
	public String getEncrypt(String pwd, String salt) {
		String result = "";
		
		try {
			// SHA 256 알고리즘 객체생성
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			log.info("md >>> " + md);
			log.info("pwd+salt >>> " + pwd + salt);
			
			String unified = pwd + salt;
			
			// update를 통해 바이트들을 난수화한다.
			md.update(unified.getBytes());
			byte[] pwdsalt = md.digest();
			
			StringBuffer sb = new StringBuffer();
			for (byte b: pwdsalt) {
				sb.append(String.format("%02x", b));
			}
			
			result = sb.toString();
			log.info("pwd + salt를 shat256 난수화작업 결과 >> " + result);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
}
