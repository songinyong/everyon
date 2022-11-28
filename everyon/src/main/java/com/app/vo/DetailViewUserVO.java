package com.app.vo;

import com.domain.jpa.Participant;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DetailViewUserVO {
	
	public Long user_id ;
	public String nickname;
	public String image;
	
	@Builder
	public DetailViewUserVO(Long user_id, String nickname, String image ) {
		this.user_id = user_id ;
		this.nickname = nickname;
		this.image = image;
	}

}
