package com.newlecture.web.service;

public interface EncryptService {
	public String getSalt();
	public String getEncrypt(String pwd, String salt);
}
