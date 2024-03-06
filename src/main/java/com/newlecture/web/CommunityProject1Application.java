package com.newlecture.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommunityProject1Application {
	
//	static {
//		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
//	}
	
	public static void main(String[] args) {
		SpringApplication.run(CommunityProject1Application.class, args);
	}
	
//	@Bean
//	public FilterRegistrationBean<CustomURLRewriter> tuckeyRegistrationBean() {
//		final FilterRegistrationBean<CustomURLRewriter> registrationBean = new FilterRegistrationBean<CustomURLRewriter>();
//		registrationBean.setFilter(new CustomURLRewriter());
//		return registrationBean;
//	}

}
