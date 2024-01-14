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
	public int addCommentMemberId(CommentEntity cEnt);
	public int deleteComment(CommentEntity cEnt);
	public CommentEntity findOneComment(CommentEntity cEnt);
	public int changeUpList(CommentEntity cEnt);
	public int changeDownList(CommentEntity cEnt);
}

