package com.ap.user.dao;

import org.apache.ibatis.annotations.Mapper;

import com.ap.user.vo.Member;

@Mapper
public interface MemberDAO {

	public void updateNewPassword(Member member);
}
