package com.service;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

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

public interface PostService {

	public Page<MainMeetDto> findAllMeeting(Pageable pageRequest, String token);
	
	public ResponseEntity<JSONObject> createMeeting(CreateMeetDto createMeetDto, String token);
	
	public ResponseEntity<JSONObject> convertFavorite(String token, Long meet_id);
	
	public Page<MainMeetDto> findCategoryMeeting(Pageable pageRequest, String category, String token);
	
	public ResponseEntity<JSONObject> applyMeet(String token, ApplyMeetDto applyDto);
	
	public Page<MainMeetDto> searchMeeting(Pageable pageRequest, String keyword, String category,  String token);
	
	public ResponseEntity<JSONObject> convertLike(String token, Long meet_id);
	
	public ResponseEntity<JSONObject> getDetailMeeting(Long meet_id, String token);
	
	public ResponseEntity<JSONObject> updateMeeting(UpdateMeetDto putMeetDto, String token);
	
	// 22/12/02 처음 생성
	public ResponseEntity<JSONObject> getMinFavoriteMeet(String token);
	
	public ResponseEntity<JSONObject> getMinLikeMeet(String token);
	
	public ResponseEntity<JSONObject> getMinJoinMeet(String token);
	
	public ResponseEntity<JSONObject> getMinCreateMeet(String token);
	
	public ResponseEntity<JSONObject> getMinApplyMeet(String token);
	
	// 22/12/03 처음 생성
	public ResponseEntity<JSONObject> findApplyList(Long meet_id, String token);
	
	public ResponseEntity<JSONObject> decideApply(PostDecideApplyDto applyDto, String token);
	
	public ResponseEntity<JSONObject> findUsersExceptOwner(Long meet_id, String token);
	
	public ResponseEntity<JSONObject> manageUserDrawUser(DelUserDrawDto drawDto, String token);
	
	public ResponseEntity<JSONObject> deleteMeet(Long meetId, String token) ;
	
	public ResponseEntity<JSONObject> outMeet(Long meetId, String token);
	
	public ResponseEntity<JSONObject> cancelApply(Long meetId, String token);
	
}
