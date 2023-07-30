package com.newlecture.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.newlecture.web.entity.TestEntity;

@Mapper
public interface TestDao {
	public List<TestEntity> getMember();
	public int isMember(TestEntity tEntity);
}
