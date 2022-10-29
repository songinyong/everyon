package com.app.api;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
		
	 @PostMapping("/allmeet")
	 public Page<MainMeetDto> pageAllMeet(@PageableDefault(size=10) Pageable pageRequest) {
	    	return postService.findAllMeeting(pageRequest);
	 }
	 
	 @PostMapping("/createmeet")
	 public ResponseEntity<JSONObject> createmeet(@RequestBody CreateMeetDto createDto) {
	    	return postService.createMeeting(createDto);
	 }
}
