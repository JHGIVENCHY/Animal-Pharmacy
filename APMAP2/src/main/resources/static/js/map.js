var infos = [];
var infos1 = [];
var markers = [];

var mapContainer = document.getElementById('map'), // 지도를 표시할 div
mapOption = { 
	center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
	level: 7 // 지도의 확대 레벨 
};
var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

// HTML5의 geolocation으로 사용할 수 있는지 확인합니다 
if (navigator.geolocation) {
	
	// GeoLocation을 이용해서 접속 위치를 얻어옵니다
	navigator.geolocation.getCurrentPosition(function(position) {
		var lat = position.coords.latitude, // 위도
		lon = position.coords.longitude; // 경도
        
        var locPosition = new kakao.maps.LatLng(lat, lon), // 마커가 표시될 위치를 geolocation으로 얻어온 좌표로 생성합니다
            //message = '<div style="padding:5px;font-family:"Noto Sans KR", sans-serif;">내 위치</div>'; // 인포윈도우에 표시될 내용입니다
			message ='<div id="div11">내 위치</div>';
        
        // 마커와 인포윈도우를 표시합니다
        displayMarker(locPosition, message);
            
      });
    
} else { // HTML5의 GeoLocation을 사용할 수 없을때 마커 표시 위치와 인포윈도우 내용을 설정합니다
    
    var locPosition = new kakao.maps.LatLng(33.450701, 126.570667),    
        message = 'geolocation을 사용할수 없어요..'
        
    displayMarker(locPosition, message);
}

// 지도에 마커와 인포윈도우를 표시하는 함수입니다
function displayMarker(locPosition, message) {
	
    // 마커를 생성합니다
    var marker = new kakao.maps.Marker({  
        map: map, 
       position: locPosition
    });
    //var message ='<div id="div11">' + msg3 + '</div>';
    // 지도 중심좌표를 접속위치로 변경합니다
    map.setCenter(locPosition);

	var iwContent = message, // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
	
	iwRemoveable = true; // removeable 속성을 ture 로 설정하면 인포윈도우를 닫을 수 있는 x버튼이 표시됩니다

	// 인포윈도우를 생성합니다
	var infowindow = new kakao.maps.InfoWindow({
	    content : iwContent,
	    removable : iwRemoveable
	});
	
	// 마커에 클릭이벤트를 등록합니다
	kakao.maps.event.addListener(marker, 'click', function() {
		// 마커 위에 인포윈도우를 표시합니다
		if(infos1.length >= 1){
			for(var i = 0; i < infos1.length; i++){
				infos1[i].close();
			}
			infos1 = [];
		}
		infowindow.open(map, marker);
		infos1.push(infowindow);
	});
}
// 시작시 좌표표시 마커
//div 클릭이벤트
function setCenter(p1, p2) {
	if(p1 == null){
		nonono();
	}
	if(p1 != null){
		var locPosition = new kakao.maps.LatLng(p1, p2);
	    map.setCenter(locPosition);
		for(var i = 0; i < markers.length; i++){
			markers[i].setMap(null);
		}
		markers = [];
	}
	
}
//a태그 클릭이벤트
function aclick(p1, p2, msg) {
	var message ='<div id="div11">' + msg + '</div>';
	var locPosition = new kakao.maps.LatLng(p1, p2);
	 // 마커를 생성합니다
    var marker = new kakao.maps.Marker({  
        map: map, 
        position: locPosition
    }); 
    markers.push(marker);
    // 지도 중심좌표를 접속위치로 변경합니다
    map.setCenter(locPosition);      

	var iwContent = message, // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
    iwRemoveable = true; // removeable 속성을 ture 로 설정하면 인포윈도우를 닫을 수 있는 x버튼이 표시됩니다

	// 인포윈도우를 생성합니다
	var infowindow = new kakao.maps.InfoWindow({
	    content : iwContent,
	    removable : iwRemoveable
	});
	if(infos1.length >= 1){
		for(var i = 0; i < infos1.length; i++){
		infos1[i].close();
		}
		infos1 = [];
	}
	if(markers.length >= 1){
		for(var i = 0; i < markers.length; i++){
			markers[i].setMap(null);
		}
		markers = [];
	}
	infowindow.open(map, marker);
	infos1.push(infowindow);
	
}
function mouseOver(p1, p2, msg) {
	if(p1 == null){
		nonono();
	}
	if(p1 != null){
		var locPosition = new kakao.maps.LatLng(p1, p2);
		 // 마커를 생성합니다
	
		map.setCenter(locPosition);
		
	    var marker = new kakao.maps.Marker({  
	        map: map, 
	        position: locPosition
	    }); 
		markers.push(marker);
		var message ='<div id="div11">' + msg + '</div>';
		var iwContent = message; // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
	
		// 인포윈도우를 생성합니다
		var infowindow = new kakao.maps.InfoWindow({
		    content : iwContent
		});
		infowindow.open(map, marker);
		infos.push(infowindow);
		
		if(markers.length >= 1){
			for(var i = 0; i < markers.length; i++){
				markers[i].setMap(null);
			}
			markers = [];
		}
	}
}

function mouseOut() {
	infosOff();
	markersOff();
}
//좌표 없을때 불러오는 메서드
function nonono(){
	
	var locPosition = new kakao.maps.LatLng(33.450701, 126.570667),    
        message = 'geolocation을 사용할수 없어요..'
    displayMarker(locPosition, message);
}	

//인포 닫고 지우는 메서드.
function infosOff() {
	if(infos.length >= 1){
		for(var i = 0; i < infos.length; i++){
			infos[i].close();
		}
		infos = [];
	}
}
//마커 지우고 없애는 메서드
function markersOff() {
		for(var i = 0; i < markers.length; i++){
			markers[i].setMap(null);
		}
		markers = [];
}
//평점 계산 메서드
function ratingToPercent(sc) {
	const score = sc * 20;
	return score + 1.5;
}
function ddd(a, a2){
	//리스트의 첫번째 값으로 이동하기.. 아직안됌.
	lat = a[0].REFINE_WGS84_LAT; // 위도
	logt = a[0].REFINE_WGS84_LOGT; //경도
	//map.setCenter(a, a2);
	if(a.length >= 1){ 
		for(var i = 0; i < a.length; i++){ // 페이지리스트만큼 반복.
			lat = a[i].REFINE_WGS84_LAT; // 위도
			logt = a[i].REFINE_WGS84_LOGT; //경도
			BIZPLC_NM = a[i].BIZPLC_NM; // 가게이름
			ROADNM_ADDR = a[i].REFINE_ROADNM_ADDR; //도로명주소
			TELNO = a[i].LOCPLC_FACLT_TELNO; // 전화번호
			//가게이름 '결과값없음.'일때 현재위치 띄우기.
			if(BIZPLC_NM == '결과값없음.'){
				var lat = position.coords.latitude,
				lon = position.coords.longitude; // 위도, 경도
		        var locPosition = new kakao.maps.LatLng(lat, lon) // 마커가 표시될 위치를 geolocation으로 얻어온 좌표로 생성합니다
			}
			else{
				
				if(TELNO == null){
					TELNO = '등록된 전화번호 없음.';
				}
				rv_score = a2[i].rv_score;
				total = a2[i].total;
				msg55 = '<div class="star-ratings">'
							+ '<div class="star-ratings-fill space-x-2 text-lg" style="width: ' + ratingToPercent(rv_score) +'%;">'
								+ '<span>★</span><span>★</span><span>★</span><span>★</span><span>★</span>'
							+ '</div>'
							+ '<div class="star-ratings-base space-x-2 text-lg">'
								+ '<span>★</span><span>★</span><span>★</span><span>★</span><span>★</span>'
							+ '</div>'
						+ '</div>';
				msg4 = '<div id="div10"><a id="a10" href="/nextindex?nm=' + BIZPLC_NM + '">' + BIZPLC_NM + '</a>'
					+ msg55 + '<div id="div9">&nbsp리뷰' + total + '</div>'
					+ '<div id="div7">' + ROADNM_ADDR + '</div><div id="div8">' + TELNO + '</div></div>';
				
				var locPosition = new kakao.maps.LatLng(lat, logt);
				displayMarker(locPosition, msg4);
			}
		}
    }
}