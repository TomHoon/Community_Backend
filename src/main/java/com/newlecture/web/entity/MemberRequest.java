package com.newlecture.web.entity;

import org.springframework.web.multipart.MultipartFile;

// fuckfuckfuck
public class MemberRequest {
	private MultipartFile mFile;
	private MemberEntity mEnt;
	
	public MultipartFile getmFile() {
		return mFile;
	}
	public void setmFile(MultipartFile mFile) {
		this.mFile = mFile;
	}
	public MemberEntity getmEnt() {
		return mEnt;
	}
	public void setmEnt(MemberEntity mEnt) {
		this.mEnt = mEnt;
	}
}
