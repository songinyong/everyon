package com.service;

import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.app.dto.ApplyMeetDto;
import com.app.dto.CreateMeetDto;
import com.app.dto.MainMeetDto;

public interface PostService {

	public Page<MainMeetDto> findAllMeeting(Pageable pageRequest, String token);
	
	public ResponseEntity<JSONObject> createMeeting(CreateMeetDto createMeetDto, String token);
	
	public ResponseEntity<JSONObject> convertFavorite(String token, Long meet_id);
	
	public Page<MainMeetDto> findCategoryMeeting(Pageable pageRequest, String category, String token);
	
	public ResponseEntity<JSONObject> applyMeet(String token, ApplyMeetDto applyDto);
}
