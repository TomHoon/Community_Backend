package com.newlecture.web.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveFile(MultipartFile multipartFile) throws IOException {
    	
//    	AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()                  
//                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
//                .build(); 
        String originalFilename = multipartFile.getOriginalFilename() + UUID.randomUUID();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, originalFilename).toString();
    }
    
    public void deleteImage(String originalFilename)  {
//    	AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()                  
//                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
//                .build();
        amazonS3.deleteObject(bucket, originalFilename);
    }
	
}