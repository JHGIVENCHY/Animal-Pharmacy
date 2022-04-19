package com.ap.consumer.service;

import com.ap.consumer.dao.dao;
import com.ap.consumer.vo.APVO;
import com.ap.consumer.vo.Row;
import com.ap.consumer.vo.scVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class MainService {
	
	@Autowired // db처리하려고.
	dao dao;
	
	String key = "5ec8756bf58f41979de53fbe8f36abba"; //인증키
	String type = "json"; //호출 문서(xml, json)
	int pIndex = 1; //페이지 위치
	int pSize = 1000; //페이지 당 요청 숫자
	String url = "https://openapi.gg.go.kr/AnimalPharmacy?KEY=" + key + "&Type=" + type + "&pIndex=" + pIndex + "&pSize=" + pSize + "&SIGUN_NM=";
	
	
    public List<Row> getAPi(){// url은 https://openapi.gg.go.kr/AnimalPharmacy?SIGUN_CD=&SIGUN_NM=구리시 의 형식
//      String url = OPENAPI_URL + "?serviceKey=" + OPENAPI_KEY;
    	RestTemplate restTemplate = new RestTemplate();
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<HttpHeaders> entity = new HttpEntity<>(headers);
    	ArrayList<Row> response4 = new ArrayList<Row>();
    	for (int i = 1; i < 5 ; i++) {
    		pIndex = i;
    		String url2 = "https://openapi.gg.go.kr/AnimalPharmacy?KEY=" + key + "&Type=" + type + "&pIndex=" + pIndex + "&pSize=" + pSize;
    		ResponseEntity<String> response2 = restTemplate.exchange(url2, HttpMethod.GET, entity, String.class);
    		List<Row> response3 = parser2(response2.getBody());
    		response4.addAll(response3);
    	}
    	return response4;
	}
    
	//파싱
	public List<Row> parser2(String Json) { 
		ObjectMapper objectMapper = new ObjectMapper();
		APVO res = null;
		try {
			res = objectMapper.readValue(Json, APVO.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		List<Row> res2 = res.AnimalPharmacy.get(1).getRow();
		
		return res2;
	}
	//
	public List<Row> parser3(List<Row> getall, String search, String si) {
		Row s = new Row();
		
		//추후 방법생각..
		s.setBIZPLC_NM("결과값없음.");
		s.setREFINE_WGS84_LAT(null);
		s.setREFINE_WGS84_LOGT(null);
		int res2length = getall.toArray().length; // 배열길이 변수.
		ArrayList<Row> list = new ArrayList<>();
		//검색값 X, 시군네임 X.
		if(search.equals("") && si.equals("시/군/구")) {
			return getall;
		}
		for(int i = 0; i < res2length; i++) {
			//이상한 데이터부터 삭제 // 약국이름 없는애들 지웠음.
			if(getall.get(i).SIGUN_NM == null) {
				getall.remove(i);
				res2length -= 1;
			}
			//주소명 없는 애들도 지움.
			if(getall.get(i).REFINE_ROADNM_ADDR == null) {
				getall.remove(i);
				res2length -= 1;
			}
			// 검색값 X, 시군네임 O.
			if(search.equals("") && getall.get(i).SIGUN_NM.indexOf(si) >= 0) {
				list.add(getall.get(i));
			}
			//검색값 O, 시군네임 X.
			if(getall.get(i).BIZPLC_NM.indexOf(search) >= 0 && si.equals("시/군/구")) {
				list.add(getall.get(i));
			}
			//검색값 O, 시군네임 O.
			if(!search.equals("") && getall.get(i).BIZPLC_NM.indexOf(search) >= 0 && getall.get(i).SIGUN_NM.indexOf(si) >= 0) {
				list.add(getall.get(i));
			}
		}
		//검색값 O 추가된거 없을시.
		if(!search.equals("") && list.toArray().length == 0) {
			list.add(s);
		}
		return  list;
    }
	public List<Row> findListPaging(List<Row> data, int page, int pageSize) { //현재 페이지
		ArrayList<Row> list = new ArrayList<Row>();
		int totaldata = data.toArray().length; // 데이터 값
		//총데이터 5개 미만일때
		if(totaldata < 5) {
			for(int i = 0; i < totaldata; i++) {
				list.add(data.get(i));
			}
		//첫페이지일때	
		}else if(page == 1) {
			for(int i = 0; i < pageSize; i++) {
				list.add(data.get(i));
			}
		//마지막페이지일때
		}else if(totaldata - page * pageSize <= 0) {
			for(int i = (pageSize * page) - pageSize; i < totaldata; i++) {
				list.add(data.get(i));
			}
		//그외
		}else{
			for(int i = (pageSize * page) - pageSize; i < pageSize * page; i++) {
				list.add(data.get(i));
			}
		}
		return list;
	}
	public List<scVO> findListPagingSc(List<Row> data) { //boardList로 db의 평점조회후 리스트에 담아옴.
		ArrayList<scVO> list = new ArrayList<scVO>();
		for(int i = 0; i < data.toArray().length; i++) {
//일단 없는 데이터 지워서 해결.	if(data.get(i).BIZPLC_NM != null && data.get(i).REFINE_ROADNM_ADDR != null) {// 둘중 한가지라도 널일경우 조회가 안되므로 둘다 널이아닐때 조회하도록 설정했음.
			//가게이름 "결과값없음." 아닐때.
			if(!data.get(i).BIZPLC_NM.equals("결과값없음.")) { 
				list.add(dao.getsc(data.get(i).BIZPLC_NM, data.get(i).REFINE_ROADNM_ADDR)); //조회할때 사용하는 값 가게명, 주소명 두가지 조건으로 조회해야 중복없음.
			}
		}
		return list;
	}
	
}
