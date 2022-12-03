package com.app.vo;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
@Getter
public class MyPageVo {

	public String nickname;
	public String image;
	public String introduce;
	
	@Builder
	public MyPageVo(String nickname, String image, String introduce ) {
		this.nickname = nickname;
		this.image = image;
		this.introduce = introduce;
	}
	
}
