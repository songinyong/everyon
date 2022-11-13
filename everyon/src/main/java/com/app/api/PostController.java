package com.app.api;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.CreateMeetDto;
import com.app.dto.MainMeetDto;
import com.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {


	private final PostService postService;
		
	 @GetMapping("/allmeet")
	 public Page<MainMeetDto> pageAllMeet(@PageableDefault(size=10) Pageable pageRequest, HttpServletRequest req) {
	    	return postService.findAllMeeting(pageRequest, req.getHeader("Authorization"));
	 }
	 
	 @PostMapping("/createmeet")
	 public ResponseEntity<JSONObject> createmeet(@RequestBody CreateMeetDto createDto, HttpServletRequest req) {
	    	return postService.createMeeting(createDto, req.getHeader("Authorization"));
	 }
	 
	 @PutMapping("/favorite")
	 public ResponseEntity<JSONObject> createmeet(HttpServletRequest req, @RequestParam("meetId") Long meetId) {
	    	return postService.convertFavorite(req.getHeader("Authorization"), meetId);
	 }
}
