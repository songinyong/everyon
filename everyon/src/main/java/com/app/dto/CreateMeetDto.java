package com.app.dto;

import com.domain.jpa.Meeting;

import lombok.Getter;

@Getter
public class CreateMeetDto {
	private int owner ;
	private String room_code;
	private int max_people;
	private String main_image;	
	private String title;
	private String description ;
	
	public CreateMeetDto(int owner, String room_code, int max_people, String main_image, String title, String description  ) {
		this.owner = owner;
		this.room_code = room_code;
		this.max_people = max_people;
		this.main_image = main_image;
		this.title = title;
		this.description = description;
	}
	
	public Meeting toEntity() {
		return Meeting.builder()
				.owner(owner)
				.room_code(room_code)
				.max_people(max_people)
				.main_image_link(main_image)
				.title(title)
				.description(description)
				.build();
	}
}
