package com.app.vo;

import lombok.Builder;

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
