package com.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.app.dto.UpdateMeetDto;
import com.domain.BaseTimeEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="meeting")
@DynamicUpdate
public class Meeting extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	@Column()
	private Long owner ;
	@Column()
	private String room_code;
	@Column()
	private int max_people;
	@Column()
	private String main_image_link;	
	@Column()
	private String title;
	@Column()
	private String description ;
	
	@Column()
	private int participant_count;
	@Column()
	private String category;
	@Column()
	private int	favorite_count;

	@Column()
	private int	like_count;
	
	
	@Builder
	public Meeting(Long owner, String room_code, int max_people, String main_image_link, String title, String description, String category, int favorite_count, int like_count, Long meet_id) {
		this.owner = owner ;
		this.room_code = room_code;
		this.max_people = max_people;
		this.main_image_link = main_image_link;
		this.title = title;
		this.description = description;
		this.participant_count = 1;
		this.favorite_count =favorite_count;
		this.like_count = like_count;
		this.category = category;
		this.id = meet_id;

	}
	
	public void increateLike_count() {
		like_count++;
	}
	
	public void decreaseLike_count() {
		like_count--;
	}
	
	public void increateParticipant_count() {
		participant_count++;
	}
	
	public void decreaseParticipant_count() {
		participant_count--;
	}
	
	public void updateInfo(UpdateMeetDto putMeetDto) {
		if(putMeetDto.getRoom_code() != null)
			this.room_code = putMeetDto.getRoom_code();
		
		if(putMeetDto.getMain_image() != null)
			this.main_image_link = putMeetDto.getMain_image();
		if(putMeetDto.getDescription() != null)
			this.description = putMeetDto.getDescription();
		if(putMeetDto.getTitle() != null)
			this.title = putMeetDto.getTitle();
		if(putMeetDto.getCategory_code() != null)
			this.category = putMeetDto.getCategory_code();
		
	}
}
