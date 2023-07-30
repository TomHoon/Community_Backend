package com.newlecture.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.newlecture.web.entity.MemberEntity;

@Mapper
public interface MemberDao {
	public List<MemberEntity> getMemberAll();
	public int joinMember(MemberEntity mEnt);
	public int loginMember(MemberEntity mEnt);
}
