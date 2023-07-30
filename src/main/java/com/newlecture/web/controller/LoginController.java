package com.newlecture.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.newlecture.web.dao.MemberDao;
import com.newlecture.web.entity.MemberEntity;

@RestController
public class LoginController {
	
	@Autowired
	MemberDao mDao;
	
	@GetMapping("/getMemberAll")
	public List<MemberEntity> getMemberAll() {
		List<MemberEntity> list = mDao.getMemberAll();
		return list;
	}
	
	@PostMapping("/loginMember")
	public int loginMember(@RequestBody MemberEntity mEnt) {
		int result;
		try {
			result = mDao.loginMember(mEnt);
		} catch (Exception e) {
			System.out.println("e : " + e);
			result = -1;
			return result;
		}
		if (result > 0) return 1;
		else return 0;
		
	}
	
	// 회원가입
	@PostMapping("/joinMember")
	public int insertMember(@RequestBody MemberEntity mEnt) {
		System.out.println("come here");
		int result;
		try {
			result = mDao.joinMember(mEnt);
		} catch (Exception e ) {
			result = -1;
			System.out.println("e : " + e);
		}
		return result;
	}
}
