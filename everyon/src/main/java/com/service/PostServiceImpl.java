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
import com.app.dto.DetailMeetDto;
import com.app.dto.MainMeetDto;
import com.app.dto.UpdateMeetDto;
import com.app.vo.DetailViewUserVO;
import com.app.vo.MinMeetVo;
import com.domain.jpa.CustomUser;
import com.domain.jpa.Favorite;
import com.domain.jpa.Like;
import com.domain.jpa.MeetApplication;
import com.domain.jpa.Meeting;
import com.domain.jpa.Participant;
import com.domain.jpa.Posts;
import com.domain.jpa.repository.FavoriteRepository;
import com.domain.jpa.repository.LikeRepository;
import com.domain.jpa.repository.MeetApplicationRepository;
import com.domain.jpa.repository.MeetRepository;
import com.domain.jpa.repository.ParticipantRepository;
import com.domain.jpa.repository.PostsRepository;
import com.domain.jpa.repository.UserRepository;
import com.util.CommonUtil;

@Service
public class PostServiceImpl implements PostService {
	
	
	private MeetRepository meetRepo ;
	private ParticipantRepository participantRepo ;
	private FavoriteRepository favoriteRepo;
	private UserRepository userRepo;
	private MeetApplicationRepository applyRepo;
	private LikeRepository likeRepo ;
	private PostsRepository postRepo;
	
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
	

	 /**
	  * 메인화면에 출력되는 카테고리 기준 게시글 목록 
	  * */
	@Transactional
    public Page<MainMeetDto> findCategoryMeeting(Pageable pageRequest, String category, String token) {
		
		 Page<Meeting> meetList = meetRepo.findMeetingByCategory(pageRequest, category);
		 Page<MainMeetDto> dtoList = meetList.map(MainMeetDto::new);
		 
		 List<Long> fv =  getFavorite(commonUtil.getUserId(token));
		 
		 dtoList.stream().filter(d -> fv.contains(d.getMeet_id())).forEach(d -> d.setFavorite());
 
		 dtoList.stream().forEach(m -> {m.setUserImages(getUploadUserImages(m.getMeet_id())); m.setMainImage(commonUtil.getImageLink(m.getMain_image())); }  );
   	return dtoList;
   }
	
	 /**
	  * 세부화면에 출력되는 방 정보
	  * */
	@Transactional
    public DetailMeetDto getDetailMeeting(Long meet_id, String token) {
		Optional<Meeting> meet = meetRepo.findById(meet_id);
		
		
		//가입하기전일때
		if(!participantRepo.findByMeetId(meet_id).stream()
	            .anyMatch(p -> p.getUserId().equals(commonUtil.getUserId(token))) ) {
			return DetailMeetDto.builder()
					.main_image(commonUtil.getImageLink(meet.get().getMain_image_link()))
					.category_code(meet.get().getCategory())
					.max_people(meet.get().getMax_people())
					.participant_count(meet.get().getParticipant_count())
					.title(meet.get().getTitle())
					.room_code(meet.get().getRoom_code())
					.meet_id(meet_id)
					.build();
		}
		//가입한후
		else {
			List<DetailViewUserVO> detailUser = new ArrayList<DetailViewUserVO>();
			Optional<Posts> post = postRepo.findByMeetId(meet_id);
			
			for(Long u : getMeetUser(meet_id)) {
				
				Optional<CustomUser> user = userRepo.findById(u);
				
				if(user.isPresent())
				detailUser.add(DetailViewUserVO.builder()
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
					.build();
			
			if(post.isPresent()) {
				detailMeetDto.setMeet_url(post.get().getMeet_url());
				detailMeetDto.setOpen_url(post.get().getOpen_url());
			}
			
			return detailMeetDto;
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
				return new ResponseEntity<JSONObject>(resultObj, HttpStatus.BAD_REQUEST);
			}
				
			
			meet_id = meetRepo.save(createMeetDto.toEntity()).getId();
			System.out.println(meet_id);
			resultObj.put("result","true");
			
			addParticipant(createMeetDto.getOwner(),meet_id );
			

			return new ResponseEntity<JSONObject>(resultObj, HttpStatus.CREATED);
		}
		catch(Exception e) {
			System.out.println(e);
    		resultObj.put("result","false");
    		resultObj.put("reason",e);
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
		
		Long user_id = commonUtil.getUserId(token);
		applyDto.setUserId(user_id);
		applyRepo.save(applyDto.toEntity());
		
		resultObj.put("result","true");
		
		return new ResponseEntity<JSONObject>(resultObj, HttpStatus.OK);
		
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
	
	
    /**
     * 즐겨찾기한 모임 검색
     * 마이페이지에서 호출
     * */
	public List<MinMeetVo> getMinFavoriteMeet(String token) {
		
	    List<MinMeetVo> list = new ArrayList<MinMeetVo>();
		for(Long id :getFavorite(commonUtil.getUserId(token))) {
			
		    Optional<Meeting> meet = meetRepo.findById(id);
		    if(meet.isPresent()) {
		    	
		    	MinMeetVo vo = MinMeetVo.builder()
						.meet_id(id)
						.title(meet.get().getTitle())
						.build();
		    	
		    	if(meet.get().getMain_image_link() != null && !meet.get().getMain_image_link().equals("") )
		    		vo.setImageLink(commonUtil.getImageLink(meet.get().getMain_image_link()));
		    	
				list.add(vo);
					
		    }
		    
		 }
		
		
		return list ;
	}
	
    /**
     * 좋아요한 모임 검색
     * 마이페이지에서 호출
     * */
	public List<MinMeetVo> getMinLikeMeet(String token) {
		
	    List<MinMeetVo> list = new ArrayList<MinMeetVo>();
		for(Like like : likeRepo.findAllByUserId(commonUtil.getUserId(token))) {
			
		    Optional<Meeting> meet = meetRepo.findById(like.getMeetId());
		    if(meet.isPresent()) {
		    	
		    	MinMeetVo vo = MinMeetVo.builder()
						.meet_id(meet.get().getId())
						.title(meet.get().getTitle())
						.build();
		    	
		    	if(meet.get().getMain_image_link() != null && !meet.get().getMain_image_link().equals("") )
		    		vo.setImageLink(commonUtil.getImageLink(meet.get().getMain_image_link()));
		    	
				list.add(vo);
					
		    }
		    
		 }
		
		
		return list ;
	}
	
    /**
     * 참가중인 모임 검색
     * -모임페이지에서 호출됨
     * */
	public List<MinMeetVo> getMinJoinMeet(String token) {
		
	    List<MinMeetVo> list = new ArrayList<MinMeetVo>();
		for(Participant p : participantRepo.findByUserId(commonUtil.getUserId(token))) {
			
		    Optional<Meeting> meet = meetRepo.findById(p.getMeetId());
		    if(meet.isPresent()) {
		    	
		    	MinMeetVo vo = MinMeetVo.builder()
						.meet_id(meet.get().getId())
						.title(meet.get().getTitle())
						.build();
		    	
		    	if(meet.get().getMain_image_link() != null && !meet.get().getMain_image_link().equals("") )
		    		vo.setImageLink(commonUtil.getImageLink(meet.get().getMain_image_link()));
		    	
				list.add(vo);
					
		    }
		    
		 }
		
		
		return list ;
	}
	
    /**
     * 개설한 모임 검색
     * -모임페이지에서 호출됨
     * */
	public List<MinMeetVo> getMinCreateMeet(String token) {
		
	    List<MinMeetVo> list = new ArrayList<MinMeetVo>();
		for(Meeting meet : meetRepo.findByOwner(commonUtil.getUserId(token))) {
			
		    MinMeetVo vo = MinMeetVo.builder()
						.meet_id(meet.getId())
						.title(meet.getTitle())
						.build();
		    	
		    if(meet.getMain_image_link() != null && !meet.getMain_image_link().equals("") )
		    	vo.setImageLink(commonUtil.getImageLink(meet.getMain_image_link()));
		    	
			list.add(vo);

		    
		 }
		
		
		return list ;
	}
	
    /**
     * 신청 대기중 모임 검색
     * -모임페이지에서 호출됨
     * */
	public ResponseEntity<JSONObject> getMinApplyMeet(String token) {
		
		JSONObject resultObj = new JSONObject(); 
		
	    List<MinMeetVo> list = new ArrayList<MinMeetVo>();
		for(Optional<MeetApplication> a : applyRepo.findByUserId(commonUtil.getUserId(token))) {
			
			if(a.isPresent()) {
			
		    Optional<Meeting> meet = meetRepo.findById(a.get().getMeetId());
		    if(meet.isPresent()) {
		    	
		    	MinMeetVo vo = MinMeetVo.builder()
						.meet_id(meet.get().getId())
						.title(meet.get().getTitle())
						.build();
		    	
		    	if(meet.get().getMain_image_link() != null && !meet.get().getMain_image_link().equals("") )
		    		vo.setImageLink(commonUtil.getImageLink(meet.get().getMain_image_link()));
		    	
				list.add(vo);
					
		    }
			}
		 }
		
		resultObj.put("result", list);
		
		return new ResponseEntity<JSONObject>(resultObj, HttpStatus.OK);
	}
}
