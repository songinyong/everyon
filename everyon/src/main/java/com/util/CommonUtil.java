/*
 * token에서 uid 추출 등 공통 서비스ㅔ
 * */

package com.util;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.config.auth.util.RequestUtil;
import com.domain.jpa.CustomUser;
import com.domain.jpa.repository.FavoriteRepository;
import com.domain.jpa.repository.UserRepository;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

@Component
public class CommonUtil {
	
	
	//후에 필요시 redis나 스프링 캐시로 변경
	private HashMap<String, Long> users = new HashMap<String, Long>();
	
	//로그인한 유저 모음
	private HashMap<Long, CustomUser> loginUser = new HashMap<Long, CustomUser>();
	private Base64.Decoder decoder = Base64.getDecoder();
    @Autowired
    private FirebaseAuth firebaseAuth;  
    
    private ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private UserRepository userRepo;
	

    public HashMap<Long, CustomUser> getUser() {
    	return loginUser ;
    }
    
    /**
     * 
     * 유효한 토큰인지 학인후 결과를 알려주는 메서드
     * return true, flase
     */
	public boolean tokenCheck(String token) {
		
        FirebaseToken decodedToken;
        
        
        try{
            
            decodedToken = firebaseAuth.verifyIdToken(RequestUtil.getAuthorizationToken(token));
            
        } catch (NullPointerException | FirebaseAuthException | IllegalArgumentException e) {
        	
        	
        	return false;
        }
        
        return true ;
		
	}
	

    /**
     * 
     * 토큰의 uid로 DB 유저 id를 가져옴
     */
	private String getTokenUid(String token)  {
		
		
		  String[] check = token.split("\\.");

          Map<String, Object> returnMap;
		try {
			returnMap = mapper.readValue(decoder.decode(check[1]), Map.class);
			return (String) returnMap.get("user_id") ;
			
		} catch (Exception e) {
			return null ;
		} 

	}
	
	
	/**
	 * 토큰의 uid 기준으로 유저의 id를 가져옴
	 * @param uid
	 * @return
	 */
	public Long getUserId(String token) {
		
		String uid = getTokenUid(token);
		
		if(users.containsKey(uid))
			return users.get(uid);
		else {
			//db구현
			CustomUser user = userRepo.findByUid(uid);
			Long id = user.getId();
			
			//캐시에 추가
			users.put(uid, id);
			loginUser.put(id, user);
			return id;
		}

	}
	
	/**
	 * 사진 링크 생성
	 */
	public String getImageLink(String img_id) {
		
		if(img_id != null)
		    return "https://dx-sprint.s3.ap-northeast-2.amazonaws.com/" + img_id ;
		else
			return "https://dx-sprint.s3.ap-northeast-2.amazonaws.com/" + "null.png" ;
	}
}
