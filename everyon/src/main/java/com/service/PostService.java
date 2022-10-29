package com.service;

import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.app.dto.CreateMeetDto;
import com.app.dto.MainMeetDto;

public interface PostService {

	public Page<MainMeetDto> findAllMeeting(Pageable pageRequest);
	
	public ResponseEntity<JSONObject> createMeeting(CreateMeetDto createMeetDto);
}
