package com.newlecture.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.newlecture.web.entity.BoardEntity;
import com.newlecture.web.entity.MemberEntity;

@Mapper
public interface BoardDao {
	public List<BoardEntity> getBoardAll();
	public List<BoardEntity> searchBoard(BoardEntity bEnt);
	public int addBoard(BoardEntity bEnt);
	public int deleteBoard(BoardEntity bEnt);
	public int updateBoard(BoardEntity bEnt);
	public int updateHitBoard(BoardEntity bEnt);
	public int updateRecommendHitBoard(BoardEntity bEnt);
}
