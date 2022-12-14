/**
 * 회원가입, 마이페이지등 유저 정보 관련된 api
 */

package com.app.api;


import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.RegisterDto;
import com.app.dto.UpdateMyPageDto;
import com.app.dto.UploadImageDto;
import com.app.vo.MyPageVo;
import com.service.CustomUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	
	private final CustomUserService userService ;
	
	@PostMapping("/register")
	public ResponseEntity<JSONObject> register(@RequestBody RegisterDto registerInfo, HttpServletRequest req) {
		

		return userService.register(registerInfo.getUid(), registerInfo.getPlatform(),  req.getHeader("Authorization"));
	}
	
	@PostMapping("/userout")
	public ResponseEntity<JSONObject> userOut(HttpServletRequest req) {
		

		return userService.out(req.getHeader("Authorization"));
	}
	@PutMapping("/imageupload")
	public ResponseEntity<JSONObject> uploadImage(@RequestBody UploadImageDto image, HttpServletRequest req) {
		
		return userService.uploadImage(req.getHeader("Authorization"), image.getImage());
	}

	
	@GetMapping("/myPage")
	public MyPageVo getMyPage(HttpServletRequest req) {
		
		return userService.getMyPage(req.getHeader("Authorization"));
		
	}
	
	@PutMapping("/myPage/update")
	public ResponseEntity<JSONObject> updateMyPage(@RequestBody UpdateMyPageDto updateMyPageDto, HttpServletRequest req) {
		
		return userService.updateMyPage(updateMyPageDto, req.getHeader("Authorization"));
		
	}
}

//@RequestHeader(value="Authorization") String token,