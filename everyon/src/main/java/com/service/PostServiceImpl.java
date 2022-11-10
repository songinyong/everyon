package com.service;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.CreateMeetDto;
import com.app.dto.MainMeetDto;
import com.domain.jpa.Meeting;
import com.domain.jpa.Participant;
import com.domain.jpa.repository.MeetRepository;
import com.domain.jpa.repository.ParticipantRepository;

@Service
public class PostServiceImpl implements PostService {
	
	
	private MeetRepository meetRepo ;
	private ParticipantRepository participantRepo ;
	
	@Autowired
	public void setMeetRepository(MeetRepository meetRepository) {
	    this.meetRepo = meetRepository;
	 }
	
	@Autowired
	public void setParticipantRepository(ParticipantRepository participantRepository) {
	    this.participantRepo = participantRepository;
	 }
	
	 /**
	  * 메인화면에 출력되는 게시글 목록 
	  * */
	@Transactional
    public Page<MainMeetDto> findAllMeeting(Pageable pageRequest) {
		
		 Page<Meeting> meetList = meetRepo.findAll(pageRequest);
		 Page<MainMeetDto> dtoList = meetList.map(MainMeetDto::new);
	
		 
		 
    	return dtoList;
    }
	
	/*
	 * 새로운 모임 등록
	 * */
	@Transactional
	public ResponseEntity<JSONObject> createMeeting(CreateMeetDto createMeetDto) {
		JSONObject resultObj = new JSONObject();  
		Long meet_id ;
		
		try {
			meet_id = meetRepo.save(createMeetDto.toEntity()).getId();
			resultObj.put("result","true");
			
			participantRepo.save(Participant.builder()
					.user_id((long) createMeetDto.getOwner())
					.meet_id(meet_id)
					.build());
			return new ResponseEntity<JSONObject>(resultObj, HttpStatus.CREATED);
		}
		catch(Exception e) {
    		resultObj.put("result","false");
    		resultObj.put("reason",e);
    		return new ResponseEntity<JSONObject>(resultObj, HttpStatus.BAD_REQUEST);
		
	    }
	}
	
	/**
	 * 즐겨찾기 추가 삭제
	 */
	
	/**
	 * 좋아요 추가
	 */
}
