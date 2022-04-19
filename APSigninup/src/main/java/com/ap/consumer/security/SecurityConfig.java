package com.ap.consumer.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Controller;

import com.ap.consumer.service.MemberService;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	/*TODO : JWT 토큰 인증에 대해 알아보기. msa로 할 경우 session이 아닌
	 * 토큰에 사용자 정보를 저장해야한다고함.*/
	
	@Autowired
	AuthFailureHandler authFailureHandler;
	
	@Autowired
	AuthSuccessHandler authSuccessHandler;
	
	@Autowired
	MemberService mService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//static resource에 관한 authentication 무시
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		System.out.println("여기까지 넘어오는거 확인");
		http.httpBasic();
		
		//접속 관련
		http.csrf().disable()
			.authorizeRequests()
			//로그인 관련 화면 접속 허용
			.antMatchers("/log/loginForm","/log/login","/log","/").permitAll()
			.antMatchers("/log/loginForm?error").permitAll()
			//회원가입 관련 화면 접속 허용
			.antMatchers("/log/signup", "/log/signupForm").permitAll()
			//아이디/비밀번호 찾기 관련 화면 접속 허용
			.antMatchers("/log/findForm","/log/findId", "/log/findPw").permitAll()
			//메인화면 및 이외의 접속으로 넘어가려면 로그인 필요
			.antMatchers("/", "/log").authenticated()
			.anyRequest().authenticated()
		
		//로그인 관련
		.and().formLogin()
			//로그인 페이지 설정
			.loginPage("/")
			//.loginPage("/loginForm")
			//로그인 url
			.loginProcessingUrl("/log/login")
			//.loginProcessingUrl("/log/success")
			//로그인 성공시 핸들러로 처리
			.successHandler(authSuccessHandler)
			//.loginPage("/log/success22")
			//로그인 실패시 핸들러로 처리
			.failureHandler(authFailureHandler)
			.permitAll()
		
		//로그아웃 관련
		.and().logout()
			//로그아웃 url
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			//로그아웃 성공시 이동 url
			.logoutSuccessUrl("http://localhost:8000/")
			//로그아웃시 세션 삭제
			.invalidateHttpSession(true)
			//로그아웃시 쿠키의 인증id 삭제
			.deleteCookies("JSESSIONID")
			.deleteCookies("mb_id")
			.permitAll();
		
		//세션 관리
		http.sessionManagement()
			//최대세션 1개
			.maximumSessions(1)
			//동시로그인 차단
			.maxSessionsPreventsLogin(false)
			//세션 만료시 이동 url
			.expiredUrl("/");

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(mService).passwordEncoder(passwordEncoder());
	}
}