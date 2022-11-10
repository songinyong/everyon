package com.app.dto;



import com.domain.jpa.Meeting;

import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class MainMeetDto {

	private int owner ;
	private String room_code;
	private int max_people;
	private int participant_count;
	private String main_image;	
	private String title;
	
	private String category;
	private boolean favorite ;
	
	public MainMeetDto(Meeting entity) {
		this.owner = entity.getOwner();
		this.room_code = entity.getRoom_code();
		this.max_people = entity.getMax_people();
		this.participant_count = entity.getParticipant_count();
		this.main_image = entity.getMain_image_link();
		this.title = entity.getTitle();
		this.favorite = false ;
		this.category = entity.getCategory();
	}
	
	public void setFavorite() {
		this.favorite = !this.favorite;
	}
	
}
