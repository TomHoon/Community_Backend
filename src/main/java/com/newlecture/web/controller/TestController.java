package com.newlecture.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.newlecture.web.dao.MemberDao;
import com.newlecture.web.dao.TestDao;
import com.newlecture.web.entity.MemberEntity;
import com.newlecture.web.entity.TestEntity;
import com.newlecture.web.service.SecurityService;

@RestController
public class TestController {
	@Autowired
	MemberDao mDao;
	
	@Autowired
    private SecurityService securityService;
    
	@Autowired
	TestDao tDao;
	
	@GetMapping("/tomhoon")
	public List<TestEntity> getMember() {
		List<TestEntity> list = new ArrayList<TestEntity>();
		list = tDao.getMember();
		return list;
	}
	
	@GetMapping("/api/test")
	public String test() {
		return "test value 입니다";
	}
	
	@PostMapping("/isMember")
	public int isMember(TestEntity tEntity) {
		int result = tDao.isMember(tEntity);
		return result;
	}
	
    @GetMapping("security/get/subject")
    public String getSubject(@RequestParam String token) {
        String subject = securityService.getSubject(token);
        return subject;
    }
    
    @GetMapping("security/generate/token")
    public Map<String, Object> generateToken(@RequestParam String subject) {
        String token = securityService.createToken(subject, 1000 * 60 * 60 * 24L);    // 24시간
        Map<String, Object> map = new HashMap<>();
        map.put("userid", subject);
        map.put("token", token);
        return map;
    }
    
    @PostMapping("test/auth")
    public String testAuth(@RequestHeader Map<String, String> headers) {
    	String id = headers.get("member_id");
    	String deTokenPw = getSubject(headers.get("token"));
    	
    	MemberEntity mEnt = new MemberEntity();
    	mEnt.setMember_id(id);
    	mEnt.setMember_pw(deTokenPw);
    	
    	MemberEntity mEnt2 = mDao.findMember(mEnt);
    	if (mEnt2.getMember_id().isEmpty()) {
    		return "일치하지 않음";
    	} else {
    		return "일치함";
    	}
    }
}
