package com.ap.consumer.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ap.consumer.pagination.Pagination;
import com.ap.consumer.service.MainService;
import com.ap.consumer.vo.Row;
import com.ap.consumer.vo.scVO;

@RestController
@RequestMapping("/")
public class MainController {
	
    MainService mainService;
    List<Row> getall;
    String [] SIGUN = {"시/군/구", "가평군",	"고양시",	"과천시",	"광명시", "광주시", "구리시", "군포시", "김포시", "남양주시", "동두천시", "부천시", "성남시", "수원시", "시흥시", 
			"안산시", "안성시", "안양시", "양주시", "양평군", "여주시", "연천군", "오산시", "용인시", "의왕시", "의정부시", "이천시", "파주시", "평택시", "포천시"
			, "하남시", "화성시"};
    public MainController(MainService mainService){
        this.mainService = mainService;
    }
	@GetMapping("/") //시작시 모든데이터가져옴

	public ModelAndView getAllAnimalPharmacy(@RequestParam(defaultValue = "1") int page){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/index.html");
		List<Row> getall = mainService.getAPi();
		this.getall = getall; // 전체데이터 담아서 전역변수로 활용.
		int totalListCnt = getall.toArray().length;// 총게시물 수
		Pagination pagination = new Pagination(totalListCnt, page);
		int pageSize = pagination.getPageSize();
		List<Row> boardList = mainService.findListPaging(getall, page, pageSize);
		List<scVO> scList = mainService.findListPagingSc(boardList);// 보드리스트를 가져다가 평점조회
		modelAndView.addObject("boardList", boardList);
		modelAndView.addObject("pagination", pagination);
		modelAndView.addObject("sisi", SIGUN);
		modelAndView.addObject("sc",scList);
		modelAndView.addObject("pageNo", page);
		return modelAndView;
    }
	//검색 , 페이지 번호 클릭시.
    @GetMapping("request")
    public ModelAndView getAnimalPharmacy(HttpServletRequest hr1, @RequestParam(defaultValue = "1") int page){
    	ModelAndView modelAndView = new ModelAndView();
    	modelAndView.setViewName("/index.html");
    	String si = hr1.getParameter("si");
		String search = hr1.getParameter("search");
		//전역변수로 만들어놓음,
//		List<Row> getall = mainService.getAPi();
		List<Row> res1 = mainService.parser3(getall, search, si);
    	int totalListCnt = res1.toArray().length;// 총게시물 수
    	Pagination pagination = new Pagination(totalListCnt, page);
    	int pageSize = pagination.getPageSize();
    	//데이터 페이징.
		List<Row> boardList = mainService.findListPaging(res1, page, pageSize);
		List<scVO> scList = mainService.findListPagingSc(boardList);
		modelAndView.addObject("search", search);
		modelAndView.addObject("si", si);
    	modelAndView.addObject("boardList", boardList);
		modelAndView.addObject("pagination", pagination);
		modelAndView.addObject("sisi", SIGUN);
		modelAndView.addObject("sc", scList);
		modelAndView.addObject("pageNo", page);
		
        return modelAndView;
    }
    // 상세페이지 만드는 조원에게 필요함.
    @GetMapping("nextindex")
    public ModelAndView nextindex(HttpServletRequest hr1){
    	ModelAndView modelAndView = new ModelAndView();
    	String nm = hr1.getParameter("nm");
    	System.out.println("넘겨받은값 : " + nm);
    	modelAndView.setViewName("/NewFile1.html");
    	modelAndView.addObject("nm", nm);
    	
        return modelAndView;
    }
}