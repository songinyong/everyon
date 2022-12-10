package com.app.dto;



import java.util.List;

import com.domain.jpa.Meeting;

import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class MainMeetDto {

	private Long meet_id;
	private Long owner ;
	private String room_code;
	private int max_people;
	private int participant_count;
	private int like_count;
	private String main_image;	
	private String title;
	private String category;
	private boolean favorite ;
	private boolean owner_check;
	private boolean join_check;
	private boolean apply_check;
	
	private List<String> user_images ;
	
	public MainMeetDto(Meeting entity) {
		this.meet_id = entity.getId();
		this.owner = entity.getOwner();
		this.room_code = entity.getRoom_code();
		this.max_people = entity.getMax_people();
		this.participant_count = entity.getParticipant_count();
		this.like_count = entity.getLike_count();
		this.main_image = entity.getMain_image_link();
		this.title = entity.getTitle();
		this.favorite = false ;
		this.owner_check = false;
		this.join_check = false;
		this.apply_check = false;
		this.category = entity.getCategory();
	}
	
	public void setFavorite() {
		this.favorite = !this.favorite;
	}
	
	public void setUserImages(List<String> images) {
		this.user_images = images ;
	}
	
	public void setMainImage(String image) {
		this.main_image = image;
	}
	
	public void setOwnerCheck() {
		this.owner_check = !this.owner_check;
	}
	
	public void setJoinCheck() {
		this.join_check = !this.join_check;
	}
	public void setApplyCheck() {
		this.apply_check = !this.apply_check;
	}
}
