/**
 * 세부화면 뷰에서 가입한 유저들의 리스트를 출력할때 사용됨
 * */

package com.app.vo;



import lombok.Builder;
import lombok.Getter;

@Getter
public class DetailViewUserVo {
	
	public Long user_id ;
	public String nickname;
	public String image;
	
	@Builder
	public DetailViewUserVo(Long user_id, String nickname, String image ) {
		this.user_id = user_id ;
		this.nickname = nickname;
		this.image = image;
	}

}
