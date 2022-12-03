package com.app.vo;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DecideApplyVo {

	private Long apply_id;
	private Long meet_id;
	private Long user_id;
	private String description;
	private LocalDateTime createdTime;
	
	@Builder
	public DecideApplyVo(Long apply_id, Long meet_id, Long user_id, String description, LocalDateTime createdTime ) {
		this.apply_id = apply_id;
		this.meet_id = meet_id;
		this.user_id = user_id;
		this.description = description;
		this.createdTime = createdTime;
	}
	
}
