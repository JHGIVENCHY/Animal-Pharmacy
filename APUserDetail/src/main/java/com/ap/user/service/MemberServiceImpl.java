package com.ap.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ap.user.dao.MemberDAO;
import com.ap.user.vo.Member;

@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private MemberDAO memberDAO;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	//비밀번호 업데이트
	public void updateNewPassword(Member member) {
		
		member.setMb_pw(passwordEncoder.encode(member.getMb_pw()));
		
		memberDAO.updateNewPassword(member);
	}
}
