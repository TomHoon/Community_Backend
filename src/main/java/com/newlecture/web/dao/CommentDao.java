package com.newlecture.web.dao;

import com.newlecture.web.entity.BoardEntity;
import com.newlecture.web.entity.CommentEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentDao {
	public List<CommentEntity> getCommentAll(CommentEntity cEnt);


}
