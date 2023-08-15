package com.newlecture.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.newlecture.web.dao.FileDao;
import com.newlecture.web.entity.FileEntity;

@RestController
public class FileController {
	
	@Autowired
	FileDao fDao;
	
	@PostMapping("/getOneFile")
	public FileEntity getOneFile(@RequestBody FileEntity fEnt) {
		return fDao.getOneFile(fEnt);
	}
}
