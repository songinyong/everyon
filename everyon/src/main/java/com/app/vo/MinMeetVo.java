package com.app.vo;

import java.util.List;

/*
 * 모임 페이지나 마이페이지에서 모임 정보를 출력할때 사용됩니다.
 * */

import lombok.Builder;
import lombok.Getter;
@Getter
public class MinMeetVo {

	private Long meet_id;
	private String title;
	private String image_link;
	private int participant_count;
	private int max_people;
	private boolean favorite_check;
	private List<DetailViewUserVo> join_list;
	
	@Builder
	public MinMeetVo(Long meet_id, String title, String image_link, int participant_count, int max_people, boolean favorite_check, List<DetailViewUserVo> join_list) {
		this.meet_id = meet_id;
		this.title = title;
		this.image_link = image_link;
		this.participant_count = participant_count;
		this.max_people = max_people;
		this.favorite_check = favorite_check;
		this.join_list = join_list;
	}
	
	public void setImageLink(String link) {
		this.image_link = link ;
	}
}
