package com.newlecture.web.entity;

import lombok.Data;

@Data
public class TestEntity {
	private String mb_id;
	private String mb_pw;
	public String getMb_id() {
		return mb_id;
	}
	public void setMb_id(String mb_id) {
		this.mb_id = mb_id;
	}
	public String getMb_pw() {
		return mb_pw;
	}
	public void setMb_pw(String mb_pw) {
		this.mb_pw = mb_pw;
	}
	
}

