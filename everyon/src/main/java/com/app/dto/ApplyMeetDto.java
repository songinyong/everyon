package com.app.dto;

import com.domain.jpa.MeetApplication;

public class ApplyMeetDto {
	
	private Long user_id ;
	private Long meet_id ;
	private String description ;
	
	public ApplyMeetDto(Long meet_id, String description ) {
		this.meet_id = meet_id;
		this.description = description ;
	}
	
	public MeetApplication toEntity() {
		
		return MeetApplication.builder()
				.meet_id(meet_id)
				.user_id(user_id)
				.description(description)
				.build();
		
	}

	public void setUserId(Long user_id) {
		this.user_id = user_id ;
		
	}
}
