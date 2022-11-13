package com.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
import com.domain.jpa.Favorite;
import com.domain.jpa.Meeting;
import com.domain.jpa.Participant;
import com.domain.jpa.repository.FavoriteRepository;
import com.domain.jpa.repository.MeetRepository;
import com.domain.jpa.repository.ParticipantRepository;
import com.domain.jpa.repository.UserRepository;
import com.util.CommonUtil;

@Service
public class PostServiceImpl implements PostService {
	
	
	private MeetRepository meetRepo ;
	private ParticipantRepository participantRepo ;
	private FavoriteRepository favoriteRepo;
	private UserRepository userRepo;
	
	//user 아이디 기준 즐겨찾기한 모음
	private HashMap<Long, List<Long>> usersFavorite = new HashMap<Long, List<Long>>();
	
	//meet 아이디 기준 가입한 user들의 프로필 사진
	private HashMap<Long, List<Long>> MeetInUser = new HashMap<Long, List<Long>>();
	
	
	private CommonUtil commonUtil;
	
	
	@Autowired
	public void setMeetRepository(MeetRepository meetRepository) {
	    this.meetRepo = meetRepository;
	 }
	
	@Autowired
	public void setParticipantRepository(ParticipantRepository participantRepository) {
	    this.participantRepo = participantRepository;
	 }

	@Autowired
	public void setFavoriteRepository(FavoriteRepository favoriteRepository) {
	    this.favoriteRepo = favoriteRepository;
	 }
	
	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepo = userRepository ;
	}
	
	@Autowired
	public void setCommonUtil(CommonUtil commonUtil) {
	    this.commonUtil = commonUtil;
	 }
	
	 /**
	  * 메인화면에 출력되는 게시글 목록 
	  * */
	@Transactional
    public Page<MainMeetDto> findAllMeeting(Pageable pageRequest, String token) {
		
		 Page<Meeting> meetList = meetRepo.findAll(pageRequest);
		 Page<MainMeetDto> dtoList = meetList.map(MainMeetDto::new);
		 
		 List<Long> fv =  getFavorite(commonUtil.getUserId(token));
		 
		 dtoList.stream().filter(d -> fv.contains(d.getMeet_id())).forEach(d -> d.setFavorite());
  
		 dtoList.stream().forEach(m -> {m.setUserImages(getUploadUserImages(m.getMeet_id())); m.setMainImage(commonUtil.getImageLink(m.getMain_image())); }  );
    	return dtoList;
    }
	
	
	//유저가 등록한 즐겨찾기 모음을 가져온다
	private List<Long> getFavorite(Long user_id) {
		if(usersFavorite.containsKey(user_id)) 
			return usersFavorite.get(user_id);	
		 else {
			
			addFavorite(user_id);
			return usersFavorite.get(user_id);
		}
	}
	
	/*
	 * 현재 캐쉬로 사용되는 해쉬맵에 추가
	 * */
	private void addFavorite(Long user_id) {
		List<Long> result = new ArrayList<Long>();
		
		Iterator<Favorite> itr = favoriteRepo.findAllByUserId(user_id).stream().iterator();

		
		while(itr.hasNext()) {
			result.add(itr.next().getMeetId());
		}
		usersFavorite.put(user_id, result);
	}
	
	/**
	 * 모임의 프로필 사진 모음
	 */
	private void updateMeetUser(Long meet_id) {
		List<Long> result = new ArrayList<Long>();
		
		Iterator<Participant> itr = participantRepo.findByMeetId(meet_id).stream().iterator();
		
		while(itr.hasNext()) {
			result.add(itr.next().getUserId());
		}
		MeetInUser.put(meet_id, result);
		
	}
	
	//모임에 가입한 유저들의 프로필 사진들을 가져온다
	private List<Long> getMeetUser(Long meet_id) {
		if(MeetInUser.containsKey(meet_id)) 
			return MeetInUser.get(meet_id);	
		 else {
			
			 updateMeetUser(meet_id);
			return MeetInUser.get(meet_id);
		}
	}
	
	//유저 아이디로 이미지 사진 가져와 반환
	private List<String> getUploadUserImages(Long meet_id) {
		List<String> images = new ArrayList<String>();
		getMeetUser(meet_id).stream().forEach(u -> images.add(commonUtil.getImageLink(userRepo.getById(u).getImage())));
		return images ;
	}
	
	
	/*
	 * 새로운 모임 등록
	 * */
	@Transactional
	public ResponseEntity<JSONObject> createMeeting(CreateMeetDto createMeetDto, String token) {
		JSONObject resultObj = new JSONObject();  
		Long meet_id ;
		
		try {
			
			createMeetDto.setOwner(commonUtil.getUserId(token));
			if(createMeetDto.getMax_people()<1) {
				resultObj.put("result","false");
				resultObj.put("reason","전체인원수는 1명 미만일 수 없습니다");
				return new ResponseEntity<JSONObject>(resultObj, HttpStatus.BAD_REQUEST);
			}
				
			
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
	 * 사용자 즐겨찾기 추가 삭제
	 */
	@Transactional
	public ResponseEntity<JSONObject> convertFavorite(String token, Long meet_id) {
		JSONObject resultObj = new JSONObject(); 
		
		Long user_id = commonUtil.getUserId(token);
		
		if(!usersFavorite.containsKey(user_id)) 
			addFavorite(user_id);
		
		if(usersFavorite.get(user_id).contains(meet_id))
			favoriteRepo.deleteByUserIdAndMeetId(user_id, meet_id);
		else {
			favoriteRepo.save(Favorite.builder()
					.user_id(user_id)
					.meet_id(meet_id).build());
		}
		addFavorite(user_id);
		resultObj.put("result","true");
		
		return new ResponseEntity<JSONObject>(resultObj, HttpStatus.OK);
			
	}
	
	/**
	 * 즐겨찾기 테이블 조회후 즐겨찾기
	 */
	
	
	/**
	 * 좋아요 추가
	 */
}
