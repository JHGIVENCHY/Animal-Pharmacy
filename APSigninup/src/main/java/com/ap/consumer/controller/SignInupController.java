package com.ap.consumer.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import com.ap.consumer.service.MemberService;
import com.ap.consumer.vo.Member;

@Controller
public class SignInupController {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	MemberService mService;
	
	@GetMapping("/")
	public String index() {
		return "login";
	}
	
	@GetMapping("/log/loginForm")
	public String loginForm(@Valid@ModelAttribute Member member,BindingResult bResult, Model model) {
		return "login";
	}
	
	@GetMapping("/log/signupForm")
	public String signupForm(Model model) {
		System.out.println("사인업폼 들어온거확인.");
		Member member = new Member(null, null, null, null, null);
		model.addAttribute("member", member);
		
		return "signup";
	}
	
	@PostMapping("/log/signup")
	public String signup(@Valid@ModelAttribute Member member,BindingResult bResult, Model model) {

		System.out.println(member.toString());
		
		Member existMember = mService.findByMbid(member.getMb_id());
		
		if(existMember != null) {
			bResult.rejectValue("mb_id", null, "이미 존재하는 아이디 입니다");
		}
		
		if(bResult.hasErrors()) {
			return "signup";
		} else {
			model.addAttribute("member", member);
			mService.insertNewAccount(member);
			
			return "redirect:http://localhost:8000/log/loginForm";
		}
	}
	@GetMapping("/log/success")
	public String success() {
		return "success"; 
		//return "redirect:success";
		}
	
	
}