package com.newlecture.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.newlecture.web.dao.BoardDao;
import com.newlecture.web.entity.BoardEntity;

@RestController
public class BoardController {
	
	@Autowired
	BoardDao bDao;
	
	@GetMapping("/getBoardAll")
	public List<BoardEntity> getBoardAll() {
		List<BoardEntity> list = bDao.getBoardAll();
		return list;
	}
	
	@PostMapping("/addBoard")
	public int addBoard(BoardEntity bEntity) {
		int result = bDao.addBoard(bEntity);
		return result;
	}
	
	@PostMapping("/searchBoard")
	public List<BoardEntity> searchBoard(BoardEntity bEntity) {
		List<BoardEntity> list = bDao.searchBoard(bEntity);
		return list;
	}
	
	@PostMapping("/updateBoard")
	public int updateBoard(BoardEntity bEntity) {
		int result = bDao.updateBoard(bEntity);
		return result;
	}
	
	@PostMapping("/updateHitBoard")
	public int updateHitBoard(BoardEntity bEntity) {
		int result = bDao.updateHitBoard(bEntity);
		return result;
	}
	
	@PostMapping("/updateRecommendBoard")
	public int updateRecommendBoard(BoardEntity bEntity) {
		int result = bDao.updateRecommendHitBoard(bEntity);
		return result;
	}
}
