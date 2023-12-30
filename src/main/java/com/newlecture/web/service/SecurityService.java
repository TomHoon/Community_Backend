package com.newlecture.web.service;

import org.springframework.stereotype.Service;

public interface SecurityService {
	String createToken(String subject, long ttlMillis);
	String getSubject(String token);
}
