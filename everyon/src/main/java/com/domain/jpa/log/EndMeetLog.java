package com.domain.jpa.log;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.domain.jpa.Meeting;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name="end_meet_log")

public class EndMeetLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	@Column()
	private Long owner ;
	@Column()
	private Long meetId ;
	
	@Column()
	private String room_code;
	@Column()
	private int max_people;
	@Column()
	private String main_image;	
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
	
	public EndMeetLog(Meeting meet) {
		this.owner = meet.getOwner();
		this.meetId = meet.getId();
		this.room_code = meet.getRoom_code();
		this.max_people = meet.getMax_people();
		this.main_image = meet.getMain_image_link();
		this.title = meet.getTitle();
		this.description = meet.getDescription();
		this.participant_count = meet.getParticipant_count();
		this.category = meet.getCategory();
		this.favorite_count = meet.getFavorite_count();
		this.like_count = meet.getLike_count();
	}
	
	
}
