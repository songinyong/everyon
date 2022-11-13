package com.service;

import java.util.Optional;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.RegisterDto;
import com.config.auth.util.RequestUtil;
import com.domain.jpa.CustomUser;
import com.domain.jpa.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.util.CommonUtil;



@Service
public class CustomUserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private FirebaseAuth firebaseAuth;  
    @Autowired
    private CommonUtil commonUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUid(username);
    }
    
    
    public CustomUser loadUserByUserUid(String uid) throws UsernameNotFoundException {
        return userRepository.findByUid(uid);
    }

    @Transactional
    private CustomUser register(String uid, String platform) {
    	
    	
        CustomUser customUser = CustomUser.builder()
                .uid(uid)
                .platform(platform)
                .build();
        userRepository.save(customUser);
        return customUser;
    }
    /*
     * 회원가입
     * */
    public ResponseEntity<JSONObject> register(String uid, String platform, String token) {
    	
        FirebaseToken decodedToken;
        JSONObject resultObj = new JSONObject();
        
        
        try{
            
            decodedToken = firebaseAuth.verifyIdToken(RequestUtil.getAuthorizationToken(token));
            
        } catch (NullPointerException | FirebaseAuthException | IllegalArgumentException e) {
        	
        	
        	resultObj.put("result", false);
        	resultObj.put("reason", e);
        	return new ResponseEntity<JSONObject>(resultObj, HttpStatus.BAD_REQUEST);
        }
    	

    	try {
    		
    		if(uid ==null || platform == null) {
    			resultObj.put("result", false);
    			resultObj.put("reason", "잘못된 요청입니다");
    		}
    		
    		
    		if(userRepository.findByUidAndPlatform(uid, platform).isEmpty()) {
        		register(uid, platform);
        		resultObj.put("result", true);
    		}
    		
    		
    		else {
    			
    			resultObj.put("result", false);
    			resultObj.put("reason", "중복된 회원이 존재합니다");
    		}
    		
    		

    	}
    	catch (Exception e) {
    		
    		resultObj.put("result", false);
    		resultObj.put("reason", e);
    		return new ResponseEntity<JSONObject>(resultObj, HttpStatus.BAD_REQUEST);
    	}
    	

    	return new ResponseEntity<JSONObject>(resultObj, HttpStatus.CREATED);
    }
    
    
    /**
     * 유저 프로필 사진 업데이트
     */
    public ResponseEntity<JSONObject> uploadImage(String token, String image) {
    	
    	JSONObject resultObj = new JSONObject();
    	
    	Optional<CustomUser> user = userRepository.findById(commonUtil.getUserId(token));
    	if(user.isPresent()) {
    		user.get().setImage(image);
    		userRepository.save(user.get());
    		resultObj.put("result", true);
    	}
    	else {
    		resultObj.put("result", false);
    		resultObj.put("reason", "해당 유저가 존재하지 않습니다");
    	}
    	
    	return new ResponseEntity<JSONObject>(resultObj, HttpStatus.OK);
    	
    }
   
}