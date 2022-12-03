/**
 * 회원관리 유저 조회 사용
 * 
 * */


package com.app.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ManageUserVo {
	public Long user_id ;
	public String nickname;
	public String image;
	
	@Builder
	public ManageUserVo(Long user_id, String nickname, String image ) {
		this.user_id = user_id ;
		this.nickname = nickname;
		this.image = image;
	}

}
