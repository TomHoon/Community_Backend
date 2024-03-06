package com.newlecture.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Configuration
public class S3Config {
	
    @Value("${cloud.aws.credentials.access-key}")
    private String iamAccessKey;
    
    @Value("${cloud.aws.credentials.secret-key}")
    private String iamSecretKey;
    
    @Value("${cloud.aws.region.static}")
    private String region;
    
    @Bean
    public AmazonS3Client amazonS3Client() throws JsonMappingException, JsonProcessingException{
    	System.out.println(">>>>>>###### " + region);
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(iamAccessKey, iamSecretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region).enablePathStyleAccess()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
    
//    @Bean
//    public AmazonS3Client amazonS3Client() {
//    	System.out.println(">>>>>>###### " + region);
//        BasicAWSCredentials credentials = new BasicAWSCredentials(iamAccessKey, iamSecretKey);
//
//        return (AmazonS3Client) AmazonS3ClientBuilder
//                .standard()
//                .withRegion(region)
//                .withCredentials(new AWSStaticCredentialsProvider(credentials))
//                .build();
//    }
 // Use this code snippet in your app.
 // If you need more information about configurations or implementing the sample
 // code, visit the AWS docs:
 // https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/home.html

 // Make sure to import the following packages in your code
 // import software.amazon.awssdk.regions.Region;
 // import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
 // import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
 // import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;	

//	 public static SecretEntity getSecret() throws JsonMappingException, JsonProcessingException {
//	
//	     String secretName = "/secret/tom";
//	     Region region = Region.of("us-east-2");
//	
//	     // Create a Secrets Manager client
//	     SecretsManagerClient client = SecretsManagerClient.builder()
//	             .region(region)
//	             .build();
//	
//	     GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
//	             .secretId(secretName)
//	             .build();
//	
//	     GetSecretValueResponse getSecretValueResponse;
//	
//	     try {
//	         getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
//	     } catch (Exception e) {
//	         // For a list of exceptions thrown, see
//	         // https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html
//	         throw e;
//	     }
//	
//	     String secret = getSecretValueResponse.secretString();
//	     ObjectMapper mapper = new ObjectMapper();
//	     SecretEntity sEnt = new SecretEntity();
//	     sEnt = mapper.readValue(secret, SecretEntity.class);
//		
//	     return sEnt;
//	 }
}
