package com.newlecture.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
    
    // "/memberUpdate",
    // "/findIdNote",
    
    // "/getAllNote",
    // "/sendList",
    // "/recvList",
    // "/findOneNote",
    // "/updateReadDate",
    // "/deleteRecv",
    // "/deleteSend",
    // "/insertNote",
    // "/countReadYN",
    // "/countRecv",
    // "/countSend",
    // "/sendListChk",
    // "/getOneFile",
    
    // "/changeUpList",
    // "/findOneComment",
    // "/deleteComment",
    // "/recommendUpDown",
    // "/addComment",
    // "/getCommentByBoard",
    // "/getAllComment",

    // "/searchBoard",
    // "/getBoardById",
    // "/updateBoard",
    // "/updateHitBoard",
    // "/deleteBoard",
    // "/updateRecommendBoard",
    // "/tempImg",
    // "/addBoard"
    private static final String[] INTERCEPTOR_BLACK_LIST = {
        "/getBoardAll",
        "/getMemberAll",
        "/getOneMember"
    };
    private static final String[] INTERCEPTOR_WHITE_LIST = {
        /** if url doesn't involved in INTERCEPTOR_BLACK_LIST, 
         *      it would pass 
         */

        /**
         * whitelist of interceptor
         * The reason why add '/error'
         *      for prevent infinite Loop
         *      -> error -> interceptor -> origin api -> error 
         */
        "/error",
        "/loginMember"
    };

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*")
				.allowedMethods("GET", "POST");
	}
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(INTERCEPTOR_WHITE_LIST);
    }

    @Bean
    public AuthenticationInterceptor authInterceptor(){
    	return new AuthenticationInterceptor();
    }
}
