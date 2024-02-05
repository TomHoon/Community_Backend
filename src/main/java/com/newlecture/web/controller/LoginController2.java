package com.newlecture.web.controller;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.newlecture.web.service.LoginService;
@RestController
@RequestMapping(value = "/login/oauth2", produces = "application/json")
public class LoginController2 {

    @Autowired
    LoginService  loginService;

    public LoginController2(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/code/{registrationId}")
    public ResponseEntity<?> googleLogin(@RequestParam String code, @PathVariable String registrationId, HttpServletResponse response) {
        loginService.socialLogin(code, registrationId);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/"));

        return new ResponseEntity<>(headers, HttpStatus.PERMANENT_REDIRECT);
    }
}