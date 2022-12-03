/**
 * 회원관리 기능에서
 * 방장이 해당되는 유저를 탈퇴시킬때 사용되는 Dto
 * */

package com.app.dto;

import lombok.Getter;

@Getter
public class DelUserDrawDto {

	private Long meetId;
	private Long userId;
	
	public DelUserDrawDto(Long userId, Long meetId) {
		this.userId = userId;
		this.meetId = meetId;
	}
}
