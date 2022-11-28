/*
 * Meet 정보를 수정하는 정보를 받기 위한 DTO
 * */

package com.app.dto;

import com.domain.jpa.Meeting;
import com.domain.jpa.Posts;

import lombok.Getter;

@Getter
public class PutMeetDto {

	private String meet_url;
	private String open_url;
	private String room_code;
	private int max_people;
	private String main_image;	
	private String title;
	private String description ;
	private String category_code;
	private Long meet_id;
	
	public PutMeetDto(String meet_url, String open_url, String room_code, int max_people, String main_image, String title, String description, String category_code, Long meet_id) {
		this.meet_url = meet_url;
		this.open_url = open_url;
		this.room_code = room_code;
		this.max_people = max_people;
		this.main_image = main_image;
		this.title = title;
		this.description = description;
		this.category_code = category_code;
		this.meet_id = meet_id;
	}
	
	public Meeting toMeetEntity() {
		return Meeting.builder()
				.meet_id(meet_id)
				.room_code(room_code)
				.max_people(max_people)
				.main_image_link(main_image)
				.title(title)
				.description(description)
				.category(category_code)
				.build();
	}
	
	public Posts toPostEntity() {
		return Posts.builder()
				.meet_id(meet_id)
				.meet_url(meet_url)
				.open_url(open_url)
				.build();
	}
}
