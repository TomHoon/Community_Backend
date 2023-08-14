
package com.newlecture.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newlecture.web.dao.CommentDao;
import com.newlecture.web.entity.CommentEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@RestController
public class CommentController {
	
	@Autowired
	CommentDao cDao;
	
	@PostMapping("/getCommentById")
	public CommentEntity getCommentById(@RequestBody CommentEntity cEntity) {
		CommentEntity cEnt = cDao.getCommentById(cEntity);
		return cEnt;
	}
}

