package com.service;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.app.dto.ApplyMeetDto;
import com.app.dto.CreateMeetDto;
import com.app.dto.DetailMeetDto;
import com.app.dto.MainMeetDto;
import com.app.dto.UpdateMeetDto;
import com.app.vo.MinMeetVo;

public interface PostService {

	public Page<MainMeetDto> findAllMeeting(Pageable pageRequest, String token);
	
	public ResponseEntity<JSONObject> createMeeting(CreateMeetDto createMeetDto, String token);
	
	public ResponseEntity<JSONObject> convertFavorite(String token, Long meet_id);
	
	public Page<MainMeetDto> findCategoryMeeting(Pageable pageRequest, String category, String token);
	
	public ResponseEntity<JSONObject> applyMeet(String token, ApplyMeetDto applyDto);
	
	public Page<MainMeetDto> searchMeeting(Pageable pageRequest, String keyword, String category,  String token);
	
	public ResponseEntity<JSONObject> convertLike(String token, Long meet_id);
	
	public DetailMeetDto getDetailMeeting(Long meet_id, String token);
	
	public ResponseEntity<JSONObject> updateMeeting(UpdateMeetDto putMeetDto, String token);
	
	public List<MinMeetVo> getMinFavoriteMeet(String token);
	
	public List<MinMeetVo> getMinLikeMeet(String token);
	
	public List<MinMeetVo> getMinJoinMeet(String token);
	
	public List<MinMeetVo> getMinCreateMeet(String token);
	
	public ResponseEntity<JSONObject> getMinApplyMeet(String token);
}
