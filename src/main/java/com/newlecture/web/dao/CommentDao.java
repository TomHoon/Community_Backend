package com.newlecture.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.newlecture.web.entity.CommentEntity;

@Mapper
public interface CommentDao {
	public List<CommentEntity> getAllComment();
	public List<CommentEntity> getCommentByBoard(CommentEntity cEnt);
	public int addComment(CommentEntity cEnt);
	public int recommendUpDown(CommentEntity cEnt);
}
