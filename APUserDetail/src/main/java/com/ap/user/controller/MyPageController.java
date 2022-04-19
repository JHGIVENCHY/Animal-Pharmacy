package com.ap.user.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ap.user.service.MemberService;
import com.ap.user.vo.Member;

@Controller
public class MyPageController {
	@Autowired
	MemberService mbService;
	String mb_id = null;
	
	@GetMapping("/mypage")
	public String mypage(HttpServletRequest req1, HttpServletResponse res1, Model model) {
		Cookie[] ck1 = req1.getCookies(); 
		for(Cookie c : ck1) {
			if (c.getName().equals("mb_id")) {
				mb_id = c.getValue();
			}
			model.addAttribute("mb_id", mb_id);
		}
		System.out.println("아이디 : " + mb_id);
		
		Member member = new Member(mb_id, null, null, null, null);
		model.addAttribute("member", member);
		member.setMb_id(mb_id);
		return "mypage";
	}
	
	@PostMapping("/mypage/changePw")
	public String findPwByIdAndNameAndMail(@Valid@ModelAttribute Member member, BindingResult bResult, Model model) throws Exception {
		if(bResult.hasErrors()) {
			model.addAttribute("mb_id", mb_id);
			return "mypage";
		} else {
			model.addAttribute("member", member);
			mbService.updateNewPassword(member);
			return "redirect:http://localhost:8000/mypage";
		}
	}
}
