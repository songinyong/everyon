package com.app.dto;



import com.domain.jpa.Meeting;

import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class MainMeetDto {

	private int owner ;
	private String room_code;
	private int num_people;
	private String main_image;	
	private String title;
	private String description ;
	
	public MainMeetDto(Meeting entity) {
		this.owner = entity.getOwner();
		this.room_code = entity.getRoom_code();
		this.num_people = entity.getNum_people();
		this.main_image = entity.getMain_image();
		this.title = entity.getTitle();
		this.description = entity.getDescription();
	}
	
}
