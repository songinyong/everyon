package com.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.ApplyMeetDto;
import com.app.dto.CreateMeetDto;
import com.app.dto.DelMeetDto;
import com.app.dto.DelUserDrawDto;
import com.app.dto.DetailMeetDto;
import com.app.dto.MainMeetDto;
import com.app.dto.PostDecideApplyDto;
import com.app.dto.UpdateMeetDto;
import com.app.vo.DecideApplyVo;
import com.app.vo.DetailViewUserVo;
import com.app.vo.ManageUserVo;
import com.app.vo.MinMeetVo;
import com.domain.jpa.CustomUser;
import com.domain.jpa.Favorite;
import com.domain.jpa.Like;
import com.domain.jpa.MeetApplication;
import com.domain.jpa.Meeting;
import com.domain.jpa.Participant;
import com.domain.jpa.Posts;
import com.domain.jpa.log.ApplyLog;
import com.domain.jpa.log.EndMeetLog;
import com.domain.jpa.log.ManageUserLog;
import com.domain.jpa.repository.FavoriteRepository;
import com.domain.jpa.repository.LikeRepository;
import com.domain.jpa.repository.MeetApplicationRepository;
import com.domain.jpa.repository.MeetRepository;
import com.domain.jpa.repository.ParticipantRepository;
import com.domain.jpa.repository.PostsRepository;
import com.domain.jpa.repository.UserRepository;
import com.domain.jpa.repository.log.ApplyLogRepository;
import com.domain.jpa.repository.log.EndMeetLogRepository;
import com.domain.jpa.repository.log.ManageUserLogRepository;
import com.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class PostServiceImpl implements PostService {
	
	
	private MeetRepository meetRepo ;
	private ParticipantRepository participantRepo ;
	private FavoriteRepository favoriteRepo;
	private UserRepository userRepo;
	private MeetApplicationRepository applyRepo;
	private LikeRepository likeRepo ;
	private PostsRepository postRepo;
	private ApplyLogRepository applyLogRepo;
	private ManageUserLogRepository manageLogRepo;
	private EndMeetLogRepository endMeetLogRepo;
	
	//user 아이디 기준 즐겨찾기한 모음 캐시용
	private HashMap<Long, List<Long>> usersFavorite = new HashMap<Long, List<Long>>();
	
	//meet 아이디 기준 가입한 user들의 프로필 사진 캐시용
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
	
	@Autowired
	public void setMeetApplicatonRepository(MeetApplicationRepository applyRepository) {
		this.applyRepo = applyRepository ;
	}
	
	@Autowired
	public void setLikeRepository(LikeRepository likeRepository) {
		this.likeRepo = likeRepository ;
	}
	
	@Autowired
	public void setPostsRepository(PostsRepository postRepository) {
		this.postRepo = postRepository;
	}
	
	@Autowired
	public void setApplyLogRepository(ApplyLogRepository applyLogRepository) {
		this.applyLogRepo = applyLogRepository;
	}
	@Autowired
	public void setManageUserLogRepository(ManageUserLogRepository manageLogRepository) {
		this.manageLogRepo = manageLogRepository;
	}
	@Autowired
	public void setEndMeetLogRepository(EndMeetLogRepository endMeetLogRepository) {
		this.endMeetLogRepo = endMeetLogRepository;
	}
	
	 /**
	  * 메인화면에 출력되는 게시글 목록 
	  * */
	@Transactional
    public Page<MainMeetDto> findAllMeeting(Pageable pageRequest, String token) {
		
		 Page<Meeting> meetList = meetRepo.findAll(pageRequest);
		 Page<MainMeetDto> dtoList = meetList.map(MainMeetDto::new);
		 Long userId = commonUtil.getUserId(token);
		 List<Long> fv =  getFavorite(userId);
		 
		 dtoList.stream().filter(d -> fv.contains(d.getMeet_id())).forEach(d -> d.setFavorite());
		 dtoList.stream().filter(d -> d.getOwner().equals(userId)).forEach(d -> d.setOwnerCheck());
		 dtoList.stream().filter(d -> getMeetUser(d.getMeet_id()).contains(userId)).forEach(d -> d.setJoinCheck());
  
		 dtoList.stream().forEach(m -> {m.setUserImages(getUploadUserImages(m.getMeet_id())); m.setMainImage(commonUtil.getImageLink(m.getMain_image())); } );
    	return dtoList;
    }
	

	 /**
	  * 메인화면에 출력되는 카테고리 기준 게시글 목록 
	  * */
	@Transactional
    public Page<MainMeetDto> findCategoryMeeting(Pageable pageRequest, String category, String token) {
		
		 Page<Meeting> meetList = meetRepo.findMeetingByCategory(pageRequest, category);
		 Page<MainMeetDto> dtoList = meetList.map(MainMeetDto::new);
		 Long userId = commonUtil.getUserId(token);
		 List<Long> fv =  getFavorite(userId);
		 
		 dtoList.stream().filter(d -> fv.contains(d.getMeet_id())).forEach(d -> d.setFavorite());
		 dtoList.stream().filter(d -> d.getOwner().equals(userId)).forEach(d -> d.setOwnerCheck());
		 dtoList.stream().filter(d -> getMeetUser(d.getMeet_id()).contains(userId)).forEach(d -> d.setJoinCheck());
 
		 dtoList.stream().forEach(m -> {m.setUserImages(getUploadUserImages(m.getMeet_id())); m.setMainImage(commonUtil.getImageLink(m.getMain_image())); }  );
   	return dtoList;
   }
	
	 /**
	  * 세부화면에 출력되는 방 정보
	  * */
	@Transactional
    public ResponseEntity<JSONObject> getDetailMeeting(Long meet_id, String token) {
		Optional<Meeting> meet = meetRepo.findById(meet_id);
		Long userId = commonUtil.getUserId(token);
		JSONObject resultObj = new JSONObject(); 
		
		if(meet.isEmpty()) {
			resultObj.put("result","false");
			resultObj.put("reason","meet_id가 없습니다.");
			return new ResponseEntity<JSONObject>(resultObj, HttpStatus.BAD_REQUEST);
		}

		//가입하기전일때
		if(!participantRepo.findByMeetId(meet_id).stream()
	            .anyMatch(p -> p.getUserId().equals(userId)) ) {
			
			DetailMeetDto detailMeetDto = DetailMeetDto.builder()
					.main_image(commonUtil.getImageLink(meet.get().getMain_image_link()))
					.category_code(meet.get().getCategory())
					.max_people(meet.get().getMax_people())
					.participant_count(meet.get().getParticipant_count())
					.title(meet.get().getTitle())
					.room_code(meet.get().getRoom_code())
					.description(meet.get().getDescription())
					.like_count(meet.get().getLike_count())	
					.meet_id(meet_id)
					.owner(meet.get().getOwner())
					.favorite_check(favoriteRepo.findByUserIdAndMeetId(userId, meet_id).isPresent())
					.like_check(likeRepo.findByUserIdAndMeetId(userId, meet_id).isPresent())
					.join_check(false)
					.build();
			
			if(meet.get().getOwner().equals(userId))
				detailMeetDto.setOwner_check(true);
			
			resultObj.put("result","true");
			resultObj.put("content",detailMeetDto);
			return new ResponseEntity<JSONObject>(resultObj, HttpStatus.OK);
		}
		//가입한후
		else {
			List<DetailViewUserVo> detailUser = new ArrayList<DetailViewUserVo>();
			Optional<Posts> post = postRepo.findByMeetId(meet_id);
			
			for(Long u : getMeetUser(meet_id)) {
				
				Optional<CustomUser> user = userRepo.findById(u);
				
				if(user.isPresent())
				detailUser.add(DetailViewUserVo.builder()
						.image(commonUtil.getImageLink(user.get().getImage()))
						.nickname(user.get().getNickname())
						.user_id(u).build());
			}
			
			DetailMeetDto detailMeetDto = DetailMeetDto.builder()
					.main_image(commonUtil.getImageLink(meet.get().getMain_image_link()))
					.category_code(meet.get().getCategory())
					.max_people(meet.get().getMax_people())
					.participant_count(meet.get().getParticipant_count())
					.like_count(meet.get().getLike_count())
					.title(meet.get().getTitle())
					.room_code(meet.get().getRoom_code())
					.join_list(detailUser)
					.meet_id(meet_id)
					.description(meet.get().getDescription())
					.owner(meet.get().getOwner())
					.favorite_check(favoriteRepo.findByUserIdAndMeetId(userId, meet_id).isPresent())
					.like_check(likeRepo.findByUserIdAndMeetId(userId, meet_id).isPresent())
					.join_check(true)
					.build();
			
			if(post.isPresent()) {
				detailMeetDto.setMeet_url(post.get().getMeet_url());
				detailMeetDto.setOpen_url(post.get().getOpen_url());
			}
			
			if(meet.get().getOwner().equals(userId))
				detailMeetDto.setOwner_check(true);
			
			resultObj.put("result","true");
			resultObj.put("content",detailMeetDto);
			return new ResponseEntity<JSONObject>(resultObj, HttpStatus.OK);
		}
		
	}
	
	/*
	 * 모임 정보 업데이트
	 * */
	@Transactional
	public ResponseEntity<JSONObject> updateMeeting(UpdateMeetDto updateMeetDto, String token) {
		JSONObject resultObj = new JSONObject();  
		
		Optional<Meeting> meet = meetRepo.findById(updateMeetDto.getMeet_id());
		
		if(meet.isPresent()) {
			meet.get().updateInfo(updateMeetDto);
		    meetRepo.save(meet.get());
		    
		    Optional<Posts> post = postRepo.findByMeetId(updateMeetDto.getMeet_id());
		    if(post.isPresent()) {
		    	post.get().updateInfo(updateMeetDto);
		    	postRepo.save(post.get());
		    }
		    else {
		    	postRepo.save(updateMeetDto.toPostEntity());
		    }
		    
		}
		
		
		
		resultObj.put("result","true");
		
		return new ResponseEntity<JSONObject>(resultObj, HttpStatus.CREATED);
		
	}
	
	
	
	 /**
	  * 세부화면에 출력되는 방 수정
	  * 
	  * 
	  * */
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
				log.info("createMeet: 잘못된 API 호출");
				return new ResponseEntity<JSONObject>(resultObj, HttpStatus.BAD_REQUEST);
			}
				
			
			meet_id = meetRepo.save(createMeetDto.toEntity()).getId();
			createMeetDto.setMeetId(meet_id);
			

			resultObj.put("result","true");
			
			addParticipant(createMeetDto.getOwner(),meet_id );
			
			postRepo.save(createMeetDto.toPostEntity());
			

			return new ResponseEntity<JSONObject>(resultObj, HttpStatus.CREATED);
		}
		catch(Exception e) {
    		resultObj.put("result","false");
    		resultObj.put("reason","알 수 없는 이유로 호출 에러 발생");
    		log.error("createMeet:" + e);
    		return new ResponseEntity<JSONObject>(resultObj, HttpStatus.BAD_REQUEST);
		
	    }
	}
	
	/*
	 * 참여자 등록 API
	 * */
	private void addParticipant(long user_id, long meet_id) {
		participantRepo.save(Participant.builder()
				.user_id(user_id)
				.meet_id(meet_id)
				.build());
	}
	
	/*
	 * 사용자가 모임의 방장인지 확인 여부 알려줌
	 */
	private boolean checkOwnerInMeet(long user_id, long meet_id) {
		
		Optional<Meeting> meet = meetRepo.findById(meet_id);
		if(meet.isPresent()) {
			if(meet.get().getOwner().equals(user_id))
				return true ;
			else
				return false;
		}
		else {
			return false;
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
	 * 방 가입 신청
	 */
	@Transactional
	public ResponseEntity<JSONObject> applyMeet(String token, ApplyMeetDto applyDto) {
		JSONObject resultObj = new JSONObject(); 
		
		if(applyDto.getMeet_id() == null) {
			resultObj.put("result","false");
			resultObj.put("reason","meet_id가 없습니다.");
			return new ResponseEntity<JSONObject>(resultObj, HttpStatus.BAD_REQUEST);
		}
		Long user_id = commonUtil.getUserId(token);	
		
		Optional<Meeting> meet = meetRepo.findById(applyDto.getMeet_id());
		
		if(meet.isEmpty()) {
			resultObj.put("result","false");
			resultObj.put("reason","잘못된 모임입니다");
			return new ResponseEntity<JSONObject>(resultObj, HttpStatus.BAD_REQUEST);
		}
		
		if(meet.get().getMax_people() <= meet.get().getParticipant_count() ) {
			resultObj.put("result","false");
			resultObj.put("reason","모임 최대 정원에 도달했습니다.");
			return new ResponseEntity<JSONObject>(resultObj, HttpStatus.BAD_REQUEST);
		}
		
		if(applyRepo.findByUserIdAndMeetId(user_id, applyDto.getMeet_id()).isEmpty()) {
			
			if(meet.get().getRoom_code().equals("pub")) {
				participantRepo.save(Participant.builder()
						.meet_id(applyDto.getMeet_id())
						.user_id(user_id)
						.build());
				
				meet.get().increateParticipant_count();
				meetRepo.save(meet.get());
				
				updateMeetUser(meet.get().getId());
				
		    }
			else if(meet.get().getRoom_code().equals("per")) {
				applyDto.setUserId(user_id);
				applyRepo.save(applyDto.toEntity());
			}
			
			//후에 방 코드가 추가된다면 여기
			else {
				
			}
			
			
			
		}
		else {
			
			resultObj.put("result","false");
			resultObj.put("reason","이미 동일한 모임 신청이 존재합니다.");
			return new ResponseEntity<JSONObject>(resultObj, HttpStatus.BAD_REQUEST);
		}
		
		
		resultObj.put("result","true");
		return new ResponseEntity<JSONObject>(resultObj, HttpStatus.OK);
		
	}
	
	
	
	/**
	 * 특정 모임에 등록된 신청 목록 조회
	 * 12/02 처음 메서드 생성
	 * 방장만 호출 가능
	 */
	public ResponseEntity<JSONObject> findApplyList(Long meet_id, String token) {
		Optional<Meeting> meet = meetRepo.findById(meet_id);
		List<DecideApplyVo> list = new ArrayList<DecideApplyVo>();
		JSONObject resultObj = new JSONObject(); 
		
		if(meet.isPresent()) {
			if(meet.get().getOwner().equals(commonUtil.getUserId(token))) {
				for(MeetApplication m : applyRepo.findByMeetId(meet_id)) {
					list.add(DecideApplyVo.builder()
							.apply_id(m.getId())
							.meet_id(meet_id)
							.createdTime(m.getCreatedDate())
							.user_id(m.getUserId())
							.description(m.getDescription())
							.build());
				}
				
			}
			
		}
		
		resultObj.put("content",list);
		
		return new ResponseEntity<JSONObject>(resultObj, HttpStatus.OK);
	}
	
	
	/**
	 * 신청 승인할지 여부 결정
	 * 12/02 처음 메서드 생성
	 * 방장만 호출 가능
	 */
	@Transactional
	public ResponseEntity<JSONObject> decideApply(PostDecideApplyDto applyDto, String token) {
		JSONObject resultObj = new JSONObject(); 
		
		Optional<MeetApplication> apply = applyRepo.findById(applyDto.getApplyId());
		Optional<Meeting> meet = meetRepo.findById(apply.get().getMeetId());
		
		if(apply.isPresent()) {
			
			if(meet.isPresent()) {
				
				if(!checkOwnerInMeet(commonUtil.getUserId(token), apply.get().getMeetId() )) {
					resultObj.put("result","false");
					resultObj.put("reason","방장만 API 호출이 가능합니다.");
					return new ResponseEntity<JSONObject>(resultObj, HttpStatus.BAD_REQUEST);
				}
				
				if(meet.get().getMax_people() <= meet.get().getParticipant_count() ) {
					resultObj.put("result","false");
					resultObj.put("reason","모임 최대 정원에 도달했습니다.");
					return new ResponseEntity<JSONObject>(resultObj, HttpStatus.BAD_REQUEST);
				}
				
				if(applyDto.isAppr() && !checkOwnerInMeet(apply.get().getUserId(), apply.get().getMeetId() )  ) {
					addParticipant(apply.get().getUserId(), apply.get().getMeetId());
					
					applyLogRepo.save(ApplyLog.builder()
							.appr(applyDto.isAppr())
							.meet_id(apply.get().getMeetId())
							.userId(apply.get().getUserId())
							.refusalDec(applyDto.getRefusalDec())
							.build());
					
					//신청 완료된건 삭제
					applyRepo.deleteById(apply.get().getId());
					
					meet.get().increateParticipant_count();
					meetRepo.save(meet.get());
					updateMeetUser(meet.get().getId());
					
					resultObj.put("result","true");
					return new ResponseEntity<JSONObject>(resultObj, HttpStatus.OK);
				}
				
			}
		}
		
		resultObj.put("result","false");
		resultObj.put("reason","잘못된 요청입니다.");
		return new ResponseEntity<JSONObject>(resultObj, HttpStatus.BAD_REQUEST);
			
	}
	
	
	/**
	 * 좋아요 추가 or 취소
	 * 파라미터: meet_id
	 */
	@Transactional
	public ResponseEntity<JSONObject> convertLike(String token, Long meet_id) {
		
		JSONObject resultObj = new JSONObject(); 
		Long user_id = commonUtil.getUserId(token);

		if(likeRepo.findByUserIdAndMeetId(user_id, meet_id).isPresent()) {
			likeRepo.deleteByUserIdAndMeetId(user_id, meet_id);
			Meeting meet = meetRepo.getById(meet_id) ;
			meet.decreaseLike_count();
			meetRepo.save(meet);
		}
		else {
			likeRepo.save(Like.builder()
					.user_id(user_id)
					.meet_id(meet_id).build());
			
			Meeting meet = meetRepo.getById(meet_id) ;
			meet.increateLike_count();
			meetRepo.save(meet);
		}

		resultObj.put("result","true");
		
		return new ResponseEntity<JSONObject>(resultObj, HttpStatus.OK);
			
	}
	/**
	 * 통합검색
	 * 파라미터: keyword:url, category_code:body
	 * */
	@Transactional
    public Page<MainMeetDto> searchMeeting(Pageable pageRequest, String keyword, String category,  String token) {
		Page<Meeting> meetList;
		 if(!keyword.equals(" ") && !category.equals(" "))
		     meetList = meetRepo.findStoreByKeywordAndCategory(pageRequest, keyword, category);
		 else if(keyword.equals(" ") && !category.equals(" "))
			 meetList = meetRepo.findMeetingByCategory(pageRequest, category);
		 else if(!keyword.equals(" ") && category.equals(" ") )
			 meetList = meetRepo.findStoreByKeyword(pageRequest, keyword);
		 else
			 meetList = meetRepo.findAll(pageRequest);
		 Page<MainMeetDto> dtoList = meetList.map(MainMeetDto::new);
		 
		 List<Long> fv =  getFavorite(commonUtil.getUserId(token));
		 
		 dtoList.stream().filter(d -> fv.contains(d.getMeet_id())).forEach(d -> d.setFavorite());
		 dtoList.stream().forEach(m -> {m.setUserImages(getUploadUserImages(m.getMeet_id())); m.setMainImage(commonUtil.getImageLink(m.getMain_image())); }  );
   	return dtoList;
   }	
	
	
	private List<DetailViewUserVo> getDetailViewUserList(Long meet_id) {
    	List<DetailViewUserVo> detailUser = new ArrayList<DetailViewUserVo>();
		for(Long u : getMeetUser(meet_id)) {
			Optional<CustomUser> user = userRepo.findById(u);
			
			if(user.isPresent())
			detailUser.add(DetailViewUserVo.builder()
					.image(commonUtil.getImageLink(user.get().getImage()))
					.nickname(user.get().getNickname())
					.user_id(u).build());
		}
		return detailUser;
	}
	
    /**
     * 즐겨찾기한 모임 검색
     * 마이페이지에서 호출
     * */
	public ResponseEntity<JSONObject> getMinFavoriteMeet(String token) {
		
		JSONObject resultObj = new JSONObject(); 
		Long userId = commonUtil.getUserId(token);
	    List<MinMeetVo> list = new ArrayList<MinMeetVo>();
	    
	    
		for(Long id : getFavorite(userId)) {
			
		    Optional<Meeting> meet = meetRepo.findById(id);
		    if(meet.isPresent()) {
		    	
		    	MinMeetVo vo = MinMeetVo.builder()
						.meet_id(id)
						.title(meet.get().getTitle())
						.max_people(meet.get().getMax_people())
						.participant_count(meet.get().getParticipant_count())
						.favorite_check(true)
						.join_list(getDetailViewUserList(id))
						.build();
		    	
		    	if(meet.get().getMain_image_link() != null && !meet.get().getMain_image_link().equals("") )
		    		vo.setImageLink(commonUtil.getImageLink(meet.get().getMain_image_link()));
		    	
				list.add(vo);
					
		    }
		    
		 }
		
		resultObj.put("content", list);
		
		return new ResponseEntity<JSONObject>(resultObj, HttpStatus.OK);
	}
	
    /**
     * 좋아요한 모임 검색
     * 마이페이지에서 호출
     * */
	public ResponseEntity<JSONObject> getMinLikeMeet(String token) {
		
		JSONObject resultObj = new JSONObject(); 
		Long userId = commonUtil.getUserId(token);
		
	    List<MinMeetVo> list = new ArrayList<MinMeetVo>();
		for(Like like : likeRepo.findAllByUserId(userId)) {
			
		    Optional<Meeting> meet = meetRepo.findById(like.getMeetId());
		    if(meet.isPresent()) {
		    	
		    	MinMeetVo vo = MinMeetVo.builder()
						.meet_id(meet.get().getId())
						.title(meet.get().getTitle())
						.max_people(meet.get().getMax_people())
						.participant_count(meet.get().getParticipant_count())
						.favorite_check(getFavorite(userId).contains(meet.get().getId()))
						.join_list(getDetailViewUserList(meet.get().getId()))
						.build();
		    	
		    	if(meet.get().getMain_image_link() != null && !meet.get().getMain_image_link().equals("") )
		    		vo.setImageLink(commonUtil.getImageLink(meet.get().getMain_image_link()));
		    	
				list.add(vo);
					
		    }
		    
		 }
		
		resultObj.put("content", list);
		
		return new ResponseEntity<JSONObject>(resultObj, HttpStatus.OK);
	}
	
    /**
     * 참가중인 모임 검색
     * -모임페이지에서 호출됨
     * */
	public ResponseEntity<JSONObject> getMinJoinMeet(String token) {
		
		JSONObject resultObj = new JSONObject(); 
		Long userId = commonUtil.getUserId(token);
	    List<MinMeetVo> list = new ArrayList<MinMeetVo>();
		for(Participant p : participantRepo.findByUserId(userId)) {
			
		    Optional<Meeting> meet = meetRepo.findById(p.getMeetId());
		    if(meet.isPresent()) {
		    	
		    	MinMeetVo vo = MinMeetVo.builder()
						.meet_id(meet.get().getId())
						.title(meet.get().getTitle())
						.max_people(meet.get().getMax_people())
						.participant_count(meet.get().getParticipant_count())
						.favorite_check(getFavorite(userId).contains(meet.get().getId()))
						.join_list(getDetailViewUserList(meet.get().getId()))
						.build();
		    	
		    	if(meet.get().getMain_image_link() != null && !meet.get().getMain_image_link().equals("") )
		    		vo.setImageLink(commonUtil.getImageLink(meet.get().getMain_image_link()));
		    	
				list.add(vo);
					
		    }
		    
		 }
		
		resultObj.put("content", list);
		
		return new ResponseEntity<JSONObject>(resultObj, HttpStatus.OK);
	}
	
    /**
     * 개설한 모임 검색
     * -모임페이지에서 호출됨
     * */
	public ResponseEntity<JSONObject> getMinCreateMeet(String token) {
		
		JSONObject resultObj = new JSONObject(); 
		Long userId = commonUtil.getUserId(token);
	    List<MinMeetVo> list = new ArrayList<MinMeetVo>();
		for(Meeting meet : meetRepo.findByOwner(userId)) {
			
		    MinMeetVo vo = MinMeetVo.builder()
						.meet_id(meet.getId())
						.title(meet.getTitle())
						.max_people(meet.getMax_people())
						.participant_count(meet.getParticipant_count())
						.favorite_check(getFavorite(userId).contains(meet.getId()))
						.join_list(getDetailViewUserList(meet.getId()))
						.build();
		    	
		    if(meet.getMain_image_link() != null && !meet.getMain_image_link().equals("") )
		    	vo.setImageLink(commonUtil.getImageLink(meet.getMain_image_link()));
		    	
			list.add(vo);

		    
		 }
		
		resultObj.put("content", list);
		
		return new ResponseEntity<JSONObject>(resultObj, HttpStatus.OK);
	}
	
    /**
     * 신청 대기중 모임 검색
     * -모임페이지에서 호출됨
     * */
	public ResponseEntity<JSONObject> getMinApplyMeet(String token) {
		
		JSONObject resultObj = new JSONObject(); 
		Long userId = commonUtil.getUserId(token);
	    List<MinMeetVo> list = new ArrayList<MinMeetVo>();
		for(Optional<MeetApplication> a : applyRepo.findByUserId(userId)) {
			
			if(a.isPresent()) {
			
		    Optional<Meeting> meet = meetRepo.findById(a.get().getMeetId());
		    if(meet.isPresent()) {
		    	
		    	MinMeetVo vo = MinMeetVo.builder()
						.meet_id(meet.get().getId())
						.title(meet.get().getTitle())
						.max_people(meet.get().getMax_people())
						.participant_count(meet.get().getParticipant_count())
						.favorite_check(getFavorite(userId).contains(meet.get().getId()))
						.join_list(getDetailViewUserList(meet.get().getId()))
						.build();
		    	
		    	if(meet.get().getMain_image_link() != null && !meet.get().getMain_image_link().equals("") )
		    		vo.setImageLink(commonUtil.getImageLink(meet.get().getMain_image_link()));
		    	
				list.add(vo);
					
		    }
			}
		 }
		
		resultObj.put("content", list);
		
		return new ResponseEntity<JSONObject>(resultObj, HttpStatus.OK);
	}
	
	/**
	 * 회원관리 기능
	 * 22/12/03 처음 메서드 생성
	 * - 방장일 경우 방장을 제외한 유저들의 목록을 보여줌
	 * */
	public ResponseEntity<JSONObject> findUsersExceptOwner(Long meet_id, String token) {
		JSONObject resultObj = new JSONObject(); 
		List<ManageUserVo> manageUser = new ArrayList<ManageUserVo>();
		if(checkOwnerInMeet(commonUtil.getUserId(token), meet_id )) {
			List<Long> users = getMeetUser(meet_id);
			users.remove((Object) commonUtil.getUserId(token));
			
			for(Long u : users) {
				Optional<CustomUser> user = userRepo.findById(u);
				
				if(user.isPresent())
					manageUser.add(ManageUserVo.builder()
						.image(commonUtil.getImageLink(user.get().getImage()))
						.nickname(user.get().getNickname())
						.user_id(u).build());
			}
			
		}
		
		resultObj.put("content", manageUser);
		return new ResponseEntity<JSONObject>(resultObj, HttpStatus.OK);
		
	}
	
	/**
	 * 회원관리 기능
	 * 22/12/03 처음 메서드 생성
	 * - 방장일 경우 방장을 제외한 유저의 상태를 변경가능
	 * 0: 탈퇴처리
	 * -후에 기능 업데이트 가능
	 * */
	public ResponseEntity<JSONObject> manageUserDrawUser(DelUserDrawDto drawDto, String token) {
		JSONObject resultObj = new JSONObject(); 
		if(checkOwnerInMeet(commonUtil.getUserId(token), drawDto.getMeetId())) {
			
			Optional<Participant> member = participantRepo.findByUserIdAndMeetId(drawDto.getUserId(), drawDto.getMeetId());
			Optional<Meeting> meet = meetRepo.findById(drawDto.getMeetId());
			
			if(!checkOwnerInMeet(drawDto.getUserId(), drawDto.getMeetId()) && member.isPresent() && meet.isPresent()) {
				manageLogRepo.save(ManageUserLog.builder()
						.meet_id(drawDto.getMeetId())
						.userId(drawDto.getUserId())
						.status_code(0)
						.build());
				
				participantRepo.delete(member.get());
				
				meet.get().decreaseParticipant_count();
				meetRepo.save(meet.get());
				updateMeetUser(meet.get().getId());
				
				resultObj.put("result", true);
				return new ResponseEntity<JSONObject>(resultObj, HttpStatus.OK);
			}
			else {
				resultObj.put("result", false);
				resultObj.put("reason", "요청 변수가 잘못 입력되었습니다.");
				return new ResponseEntity<JSONObject>(resultObj, HttpStatus.BAD_REQUEST);
			}
			
		}
		resultObj.put("result", false);
		resultObj.put("reason", "방장만 요청 가능합니다.");
		return new ResponseEntity<JSONObject>(resultObj, HttpStatus.BAD_REQUEST);
		
	}
	
	
	/**
	 * 모임 삭제
	 * 22/12/03 처음 메서드 생성
	 * -방장일 경우 모임 삭제
	 * */
	public ResponseEntity<JSONObject> deleteMeet(Long meetId, String token) {
		JSONObject resultObj = new JSONObject(); 
		Optional<Meeting> meet = meetRepo.findById(meetId);
		
		if(meet.isPresent()) {
			if(checkOwnerInMeet(commonUtil.getUserId(token), meetId)) {
				
				endMeetLogRepo.save(new EndMeetLog(meet.get()));
				//applyRepo.deleteAllByMeetId(meetId);
				//participantRepo.deleteAllByMeetId(meetId);
				meetRepo.deleteById(meetId);
				
				resultObj.put("result", true);
				return new ResponseEntity<JSONObject>(resultObj, HttpStatus.OK);	
				
			}
			
		}
		
		resultObj.put("result", false);
		resultObj.put("reason", "잘못된 요청입니다.");
		return new ResponseEntity<JSONObject>(resultObj, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * 모임 나가기
	 * 22/12/04 처음 메서드 생성
	 * 방장일 경우 호출 안됨
	 * */
	public ResponseEntity<JSONObject> outMeet(Long meetId, String token) {
		JSONObject resultObj = new JSONObject(); 
		Optional<Meeting> meet = meetRepo.findById(meetId);
		
		if(meet.isPresent()) {
			if(!checkOwnerInMeet(commonUtil.getUserId(token), meetId)) {
				
				Optional<Participant> participant = participantRepo.findByUserIdAndMeetId(commonUtil.getUserId(token), meetId);
				
				if(participant.isPresent()) {
					participantRepo.delete(participant.get());
					//applyRepo.deleteAllByMeetId(meetId);
					//participantRepo.deleteAllByMeetId(meetId);
					meet.get().decreaseParticipant_count();
					meetRepo.save(meet.get());
					updateMeetUser(meet.get().getId());
					resultObj.put("result", true);
					return new ResponseEntity<JSONObject>(resultObj, HttpStatus.OK);
				}
			}

		}
		
		resultObj.put("result", false);
		resultObj.put("reason", "잘못된 요청입니다.");
		return new ResponseEntity<JSONObject>(resultObj, HttpStatus.BAD_REQUEST);
	}
	
	
	/**
	 * 모임가입 신청 취소하기
	 */
	public ResponseEntity<JSONObject> cancelApply(Long meetId, String token) {
		JSONObject resultObj = new JSONObject(); 
		Optional<Meeting> meet = meetRepo.findById(meetId);
		
		if(meet.isPresent()) {
			if(!checkOwnerInMeet(commonUtil.getUserId(token), meetId)) {
				
				List<Optional<MeetApplication>> apply = applyRepo.findByUserIdAndMeetId(commonUtil.getUserId(token), meetId);
				
				if(apply.get(0).isPresent()) {
				

					applyRepo.delete(apply.get(0).get());
					resultObj.put("result", true);
					return new ResponseEntity<JSONObject>(resultObj, HttpStatus.OK);
				
				}
				
			}
			
		}
		
		resultObj.put("result", false);
		resultObj.put("reason", "잘못된 요청입니다.");
		return new ResponseEntity<JSONObject>(resultObj, HttpStatus.BAD_REQUEST);
	}
	
}
