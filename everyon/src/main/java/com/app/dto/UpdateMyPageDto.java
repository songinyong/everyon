package com.app.dto;

import com.app.vo.MyPageVo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateMyPageDto {

	public String nickname;
	public String image;
	public String introduce;
	

	public UpdateMyPageDto(String nickname, String image, String introduce ) {
		this.nickname = nickname;
		this.image = image;
		this.introduce = introduce;
	}
	
	
}
