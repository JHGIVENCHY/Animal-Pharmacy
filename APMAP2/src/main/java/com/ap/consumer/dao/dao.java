package com.ap.consumer.dao;

import org.apache.ibatis.annotations.Mapper;

import com.ap.consumer.vo.scVO;

@Mapper // mapping 을 위해 xml에서 정의한다는
public interface dao {

	public scVO getsc(String nm, String rd);
}
