package com.app.dto;



import lombok.Getter;

@Getter
public class DelMeetDto {

	private Long meet_id;
	
	public DelMeetDto(Long meet_id) {
		this.meet_id = meet_id;
	}
}
