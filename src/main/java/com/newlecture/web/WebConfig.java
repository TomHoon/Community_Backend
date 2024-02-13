package com.newlecture.web;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
//@EnableWebMvc
//@EnableSwagger2
public class WebConfig implements WebMvcConfigurer{
	
    /*
    Docket: Swagger 설정의 핵심이 되는 Bean
    useDefaultResponseMessages: Swagger 에서 제공해주는 기본 응답 코드 (200, 401, 403, 404). false 로 설정하면 기본 응답 코드를 노출하지 않음
    apis: api 스펙이 작성되어 있는 패키지 (Controller) 를 지정
    paths: apis 에 있는 API 중 특정 path 를 선택
    apiInfo:Swagger UI 로 노출할 정보
    */
	
//    private static final String SERVICE_NAME = "Community Project";
//    private static final String API_VERSION = "V1";
//    private static final String API_DESCRIPTION = "CommunityProject API";
//    private static final String API_URL = "http://tomhoon.duckdns.org:18080/";
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**/*")
				.addResourceLocations("classpath:/static/")
				.resourceChain(true)
				.addResolver(new PathResourceResolver() {
					@Override
					protected Resource getResource(String resourcePath, Resource location) throws IOException {
						Resource requestedResource = location.createRelative(resourcePath);
						return requestedResource.exists() && requestedResource.isReadable() ? requestedResource
								: new ClassPathResource("/static/index.html");
					}
				});
	}
		
//        registry.addResourceHandler("/swagger-ui.html")
//				.addResourceLocations("classpath:/META-INF/resources/");
//        
//		registry.addResourceHandler("/webjars/**")
//		        .addResourceLocations("classpath:/META-INF/resources/webjars/");
//		
//		registry.addResourceHandler("/route/*")
//				.addResourceLocations("classpath:/static/")
//				.resourceChain(true)
//				.addResolver(new PathResourceResolver() {
//					@Override
//					protected Resource getResource(String resourcePath, Resource location) throws IOException {
//						Resource requestedResource = location.createRelative(resourcePath);
//						return requestedResource.exists() && requestedResource.isReadable() ? requestedResource
//								: new ClassPathResource("/static/index.html");
//					}
//				});
//		
//		registry.addResourceHandler("/*")
//				.addResourceLocations("classpath:/static/")
//				.resourceChain(true)
//				.addResolver(new PathResourceResolver() {
//					@Override
//					protected Resource getResource(String resourcePath, Resource location) throws IOException {
//						Resource requestedResource = location.createRelative(resourcePath);
//						return requestedResource.exists() && requestedResource.isReadable() ? requestedResource
//								: new ClassPathResource("/static/index.html");
//					}
//		});
//		
		// -- Static resources
//		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//	}
	
//	   	@Bean
//	    public Docket api() {
//	        return new Docket(DocumentationType.SWAGGER_2)
//	                .apiInfo(apiInfo())
//	                .select()
//	                .apis(RequestHandlerSelectors.any()) // RequestMapping의 모든 URL LIST
//	                .paths(PathSelectors.any()) // .any() -> ant(/api/**") /api/**인 URL만 표시
//	                .build();
//	    }
//
//	    private ApiInfo apiInfo() {
//	        return new ApiInfoBuilder().title(SERVICE_NAME) // 서비스명
//	                .version(API_VERSION)                   // API 버전
//	                .description(API_DESCRIPTION)           // API 설명
//	                .termsOfServiceUrl(API_URL)             // 서비스 url
//	                .licenseUrl("라이센스 표시할 url")
//	                .build();
//
//	    }// API INFO
	    
}
