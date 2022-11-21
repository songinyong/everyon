package com.app.dto;

import com.domain.jpa.Meeting;

import lombok.Getter;

@Getter
public class CreateMeetDto {
	private Long owner ;
	private String room_code;
	private int max_people;
	private String main_image;	
	private String title;
	private String description ;
	private String category_code;
	
	public CreateMeetDto(String room_code, int max_people, String main_image, String title, String description, String  category_code ) {
		this.room_code = room_code;
		this.max_people = max_people;
		this.main_image = main_image;
		this.title = title;
		this.description = description;
		this.category_code = category_code;
	}
	
	public Meeting toEntity() {
		return Meeting.builder()
				.owner(owner)
				.room_code(room_code)
				.max_people(max_people)
				.main_image_link(main_image)
				.title(title)
				.description(description)
				.category(category_code)
				.favorite_count(0)
				.like_count(0)
				.build();
	}
	
	public void setOwner(Long owner) {
		this.owner = owner ;
	}
}
