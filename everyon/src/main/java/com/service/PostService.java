package com.service;

import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.app.dto.ApplyMeetDto;
import com.app.dto.CreateMeetDto;
import com.app.dto.DetailMeetDto;
import com.app.dto.MainMeetDto;
import com.app.dto.PutMeetDto;

public interface PostService {

	public Page<MainMeetDto> findAllMeeting(Pageable pageRequest, String token);
	
	public ResponseEntity<JSONObject> createMeeting(CreateMeetDto createMeetDto, String token);
	
	public ResponseEntity<JSONObject> convertFavorite(String token, Long meet_id);
	
	public Page<MainMeetDto> findCategoryMeeting(Pageable pageRequest, String category, String token);
	
	public ResponseEntity<JSONObject> applyMeet(String token, ApplyMeetDto applyDto);
	
	public Page<MainMeetDto> searchMeeting(Pageable pageRequest, String keyword, String category,  String token);
	
	public ResponseEntity<JSONObject> convertLike(String token, Long meet_id);
	
	public DetailMeetDto getDetailMeeting(Long meet_id, String token);
	
	public ResponseEntity<JSONObject> putMeeting(PutMeetDto putMeetDto, String token);
}
