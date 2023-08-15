package com.newlecture.web.dao;

import org.apache.ibatis.annotations.Mapper;

import com.newlecture.web.entity.FileEntity;

@Mapper
public interface FileDao {
	public FileEntity getOneFile(FileEntity fEnt);
}
