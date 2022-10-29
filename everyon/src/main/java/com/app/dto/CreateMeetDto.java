package com.app.dto;

import com.domain.jpa.Meeting;

public class CreateMeetDto {
	private int owner ;
	private String room_code;
	private int num_people;
	private String main_image;	
	private String title;
	private String description ;
	
	public CreateMeetDto(int owner, String room_code, int num_people, String main_image, String title, String description  ) {
		this.owner = owner;
		this.room_code = room_code;
		this.num_people = num_people;
		this.main_image = main_image;
		this.title = title;
		this.description = description;
	}
	
	public Meeting toEntity() {
		return Meeting.builder()
				.owner(owner)
				.room_code(room_code)
				.num_people(num_people)
				.main_image(main_image)
				.title(title)
				.description(description)
				.build();
	}
}
