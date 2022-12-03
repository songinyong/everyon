package com.app.dto;

import java.util.List;

import com.app.vo.DetailViewUserVo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DetailMeetDto {

	private Long meet_id;
	private String meet_url;
	private String open_url;
	private String room_code;
	private int max_people;
	private String main_image;	
	private String title;
	private String description ;
	private String category_code;
	private int	like_count;
	private int participant_count;
	//private List<DetailViewUser> apply_list ;
	private List<DetailViewUserVo> join_list;
	
	@Builder
	public DetailMeetDto(String room_code, int max_people, String main_image, String title, String description, String  category_code, Long meet_id, String meet_url, String open_url ,int	like_count, List<DetailViewUserVo> join_list, int participant_count) {
		this.room_code = room_code;
		this.max_people = max_people;
		this.main_image = main_image;
		this.title = title;
		this.description = description;
		this.category_code = category_code;		
		this.meet_id = meet_id ;
		this.meet_url = meet_url;
		this.open_url = open_url;
		this.like_count = like_count;
		//this.apply_list = apply_list;
		this.join_list = join_list;
		this.participant_count = participant_count;
	}
	
	
	
	
}
