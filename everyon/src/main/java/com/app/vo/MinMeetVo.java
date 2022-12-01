package com.app.vo;

import lombok.Builder;
import lombok.Getter;
@Getter
public class MinMeetVo {

	private Long meet_id;
	private String title;
	private String image_link;
	
	@Builder
	public MinMeetVo(Long meet_id, String title, String image_link) {
		this.meet_id = meet_id;
		this.title = title;
		this.image_link = image_link;
	}
	
	public void setImageLink(String link) {
		this.image_link = link ;
	}
}
