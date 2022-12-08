package com.app.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.ApplyMeetDto;
import com.app.dto.CreateMeetDto;
import com.app.dto.DelMeetDto;
import com.app.dto.DelUserDrawDto;
import com.app.dto.DetailMeetDto;
import com.app.dto.MainMeetDto;
import com.app.dto.PostDecideApplyDto;
import com.app.dto.UpdateMeetDto;
import com.app.vo.DecideApplyVo;
import com.app.vo.MinMeetVo;
import com.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {


	private final PostService postService;
		
	 //모임들 목록
	 @GetMapping("/allmeet")
	 public Page<MainMeetDto> pageAllMeet(@PageableDefault(size=10) Pageable pageRequest, HttpServletRequest req) {
	    	return postService.findAllMeeting(pageRequest, req.getHeader("Authorization"));
	 }
	 
	//카테고리별 모임들 목록
	 @GetMapping("/meet/{category}")
	 public Page<MainMeetDto> pageCategoryMeet(@PageableDefault(size=10) Pageable pageRequest, @PathVariable String category, HttpServletRequest req) {
	    	return postService.findCategoryMeeting(pageRequest, category, req.getHeader("Authorization"));
	 }	 
	 
	 //신규 모임 생성
	 @PostMapping("/createmeet")
	 public ResponseEntity<JSONObject> createmeet(@RequestBody CreateMeetDto createDto, HttpServletRequest req) {
	    	return postService.createMeeting(createDto, req.getHeader("Authorization"));
	 }
	 
	 //즐겨찾기 추가나 삭제
	 @PutMapping("/favorite")
	 public ResponseEntity<JSONObject> convertFavorite(HttpServletRequest req, @RequestParam("meetId") Long meetId) {
	    	return postService.convertFavorite(req.getHeader("Authorization"), meetId);
	 }

	 //좋아요 추가나 삭제
	 @PutMapping("/like")
	 public ResponseEntity<JSONObject> convertLike(HttpServletRequest req, @RequestParam("meetId") Long meetId) {
	    	return postService.convertLike(req.getHeader("Authorization"), meetId);
	 }
	 
	 //방가입 신청
	 @PostMapping("/applymeet")
	 public ResponseEntity<JSONObject> createmeet(@RequestBody ApplyMeetDto applyDto, HttpServletRequest req) {
	    	return postService.applyMeet(req.getHeader("Authorization"), applyDto);
	 }
	 
	 //검색 API
	 @GetMapping("/meet/search")
	 public Page<MainMeetDto> searchMeet(@PageableDefault(size=10) Pageable pageRequest,@RequestParam("keyword") String keyword, @RequestParam("category") String category,  HttpServletRequest req) {
	    	return postService.searchMeeting(pageRequest, keyword,category, req.getHeader("Authorization"));
	    	
	    	
	 }
	 
	 //meet_id기준 세부 정보 검색
	 @GetMapping("/meet/detailview/{meet_id}")
	 public ResponseEntity<JSONObject> detailView(@PathVariable Long meet_id,HttpServletRequest req) {
	    	return postService.getDetailMeeting(meet_id, req.getHeader("Authorization"));
	    	
	    	
	 }	 
	 
	 //meet의 값 변경
	 @PutMapping("/meet/update")
	 public ResponseEntity<JSONObject> updateMeeting(@RequestBody UpdateMeetDto updateMeetDto, String token, HttpServletRequest req) {
		 return postService.updateMeeting(updateMeetDto, req.getHeader("Authorization"));
	 }
	 
	 //
	 @GetMapping("/min/favorite")
	 public ResponseEntity<JSONObject> minFavoriteMeet( HttpServletRequest req) {
	    	return postService.getMinFavoriteMeet(req.getHeader("Authorization"));
	 }
	 
	 @GetMapping("/min/like")
	 public ResponseEntity<JSONObject> minlikeMeet( HttpServletRequest req) {
	    	return postService.getMinLikeMeet(req.getHeader("Authorization"));
	 }
	 
	 @GetMapping("/min/joinmeet")
	 public ResponseEntity<JSONObject> minjoinMeet( HttpServletRequest req) {
	    	return postService.getMinJoinMeet(req.getHeader("Authorization"));
	 }
	 
	 @GetMapping("/min/createmeet")
	 public ResponseEntity<JSONObject> minCreateMeet( HttpServletRequest req) {
	    	return postService.getMinCreateMeet(req.getHeader("Authorization"));
	 }
	 
	 @GetMapping("/min/applymeet")
	 public ResponseEntity<JSONObject> minApplyMeet( HttpServletRequest req) {
	    	return postService.getMinApplyMeet(req.getHeader("Authorization"));
	 }
	 
	 @GetMapping("/manage/apply/{meet_id}")
	 public ResponseEntity<JSONObject> manageApplyList(@PathVariable Long meet_id, HttpServletRequest req) {
	    	return postService.findApplyList(meet_id, req.getHeader("Authorization"));
	 }
	 
	 @PostMapping("/manage/decide/apply")
	 public ResponseEntity<JSONObject> decideApply(@RequestBody PostDecideApplyDto applyDto, HttpServletRequest req) {
		 return postService.decideApply(applyDto, req.getHeader("Authorization"));
	 }
	 
	 @GetMapping("/manage/users/{meet_id}")
	 public ResponseEntity<JSONObject> findUsersExceptOwner(@PathVariable Long meet_id, HttpServletRequest req) {
		 return postService.findUsersExceptOwner(meet_id, req.getHeader("Authorization"));
	 }
	 
	 @PutMapping("/manage/users")
	 public ResponseEntity<JSONObject> manageUserDrawUser(@RequestBody DelUserDrawDto drawDto, HttpServletRequest req) {
		 return postService.manageUserDrawUser(drawDto, req.getHeader("Authorization"));
	 }
	 @DeleteMapping("/delete/meet/{meet_id}")
	 public ResponseEntity<JSONObject> deleteMeet(@PathVariable Long meet_id, HttpServletRequest req) {
		 return postService.deleteMeet(meet_id, req.getHeader("Authorization"));
	 }
	 @DeleteMapping("/delete/meetout/{meet_id}")
	 public ResponseEntity<JSONObject> outMeet(@PathVariable Long meet_id, HttpServletRequest req) {
		 return postService.outMeet(meet_id, req.getHeader("Authorization"));
	 }
	 @DeleteMapping("/delete/apply/{meet_id}")
	 public ResponseEntity<JSONObject> cancelMeet(@PathVariable Long meet_id, HttpServletRequest req) {
		 return postService.cancelApply(meet_id, req.getHeader("Authorization"));
	 }
	 
	 
}
