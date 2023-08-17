package com.newlecture.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.newlecture.web.dao.CommentDao;
import com.newlecture.web.entity.CommentEntity;

@RestController
public class CommentController {
	
	@Autowired
	CommentDao cDao;
	
	@GetMapping("/getAllComment")
	public List<CommentEntity> getAllComment() {
		return cDao.getAllComment();
	}
	
	@PostMapping("/getCommentByBoard")
	public List<CommentEntity> getCommentByBoard(@RequestBody CommentEntity cEnt) {
		return cDao.getCommentByBoard(cEnt);
	}
	
	@PostMapping("/addComment")
	public int addComment(@RequestBody CommentEntity cEnt) {
		return cDao.addComment(cEnt);
	}
	
	@PostMapping("/recommendUpDown")
	public int recommendUpDown(@RequestBody CommentEntity cEnt) {
		return cDao.recommendUpDown(cEnt);
	}
}
