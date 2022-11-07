package com.domain.jpa;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

import com.domain.BaseTimeEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name="meeting")
public class Meeting extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	@Column()
	private int owner ;
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
	
	
	
	@Builder
	public Meeting(int owner, String room_code, int max_people, String main_image_link, String title, String description) {
		this.owner = owner ;
		this.room_code = room_code;
		this.max_people = max_people;
		this.main_image_link = main_image_link;
		this.title = title;
		this.description = description;
		this.participant_count = 1;
		this.favorite_count =0;

	}
}
