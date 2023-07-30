package com.newlecture.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.newlecture.web.dao.TestDao;
import com.newlecture.web.entity.TestEntity;

@RestController
public class TestController {
	
	@Autowired
	TestDao tDao;
	
	@GetMapping("/tomhoon")
	public List<TestEntity> getMember() {
		List<TestEntity> list = new ArrayList<TestEntity>();
		list = tDao.getMember();
		return list;
	}
	
	@GetMapping("/test")
	public String test() {
		return "test value 입니다";
	}
	
	@PostMapping("/isMember")
	public int isMember(TestEntity tEntity) {
		int result = tDao.isMember(tEntity);
		return result;
	}
}
