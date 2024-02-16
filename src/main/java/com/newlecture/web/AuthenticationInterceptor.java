package com.newlecture.web;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.newlecture.web.dao.MemberDao;
import com.newlecture.web.entity.MemberEntity;
import com.newlecture.web.service.EncryptServiceImpl;
import com.newlecture.web.service.SecurityServiceImpl;

import lombok.RequiredArgsConstructor;

// @RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor{
    
    @Autowired
    MemberDao mDao;

    @Autowired
    SecurityServiceImpl sSvc;

    @Autowired
    EncryptServiceImpl eSvc;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        System.out.println("interceptor >>> ");
        System.out.println("interceptor >>> " + request.getHeader("token"));
        
        HashMap<String, String> map = new HashMap<>();
        map.put("token", request.getHeader("token"));
        map.put("id", request.getHeader("id"));

        return validToken(map);
    }

    private boolean validToken(HashMap<String, String> checkMap) {
        String token = checkMap.get("token");
        String id = checkMap.get("id");
        
        /**
         * 아이디 갖고서 비번 뽑기 - a
         * 토근 갖고서 비번 뽑기 - b
         * a, b 비교하여 일치하면 true 
         */
        
         // 아이디로 비밀번호 가져오기 시작
        MemberEntity mEnt = new MemberEntity();
        mEnt.setMember_id(id);

        MemberEntity resEnt = mDao.getOneMember(mEnt);
        String res_password = resEnt.getMember_pw();
        // 아이디로 비밀번호 가져오기 끝


        // 토큰으로 비밀번호 가져오기 시작
        String origin_password = sSvc.getSubject(token);
        // 토큰으로 비밀번호 가져오기 끝
        
        if (res_password.equals(origin_password)) {
            return true;
        } else {
            return false;
        }
    }
}
