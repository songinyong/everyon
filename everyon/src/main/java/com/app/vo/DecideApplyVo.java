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
	private String name ;
	private String image_link;
	
	@Builder
	public DecideApplyVo(Long apply_id, Long meet_id, Long user_id, String description, LocalDateTime createdTime, String name, String image_link  ) {
		this.apply_id = apply_id;
		this.meet_id = meet_id;
		this.user_id = user_id;
		this.description = description;
		this.createdTime = createdTime;
		this.name = name ;
		this.image_link = image_link;
	}
	
}
