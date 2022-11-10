/*
 * token에서 uid 추출 등 공통 서비스ㅔ
 * */

package com.util;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.config.auth.util.RequestUtil;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

public class CommonUtil {
	
	
	//후에 필요시 redis나 스프링 캐시로 변경
	private HashMap<String, Integer> users = new HashMap<String, Integer>();
	private HashMap<Integer, List> usersFavorite = new HashMap<Integer, List>();
	
	private Base64.Decoder decoder = Base64.getDecoder();
    @Autowired
    private FirebaseAuth firebaseAuth;  
	
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
	public String getTokenUid(String token)  {
		
		
		  String[] check = token.split("\\.");

            
          ObjectMapper mapper = new ObjectMapper();
          Map<String, Object> returnMap;
		try {
			returnMap = mapper.readValue(decoder.decode(check[1]), Map.class);
			System.out.println(returnMap.keySet().size());
		} catch (StreamReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
          
        return "ds" ;
		
	}
	
	
	/**
	 * 토큰의 uid 기준으로 유저의 id를 가져옴
	 * @param uid
	 * @return
	 */
	public int getUserId(String token) {
		
		if(users.containsKey(token))
			return users.get(token);
		else {
			//db구현
			
			return users.get(token);
		}
		
		
	}
}
