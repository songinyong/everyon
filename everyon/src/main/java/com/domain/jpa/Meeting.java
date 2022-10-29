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
	private int num_people;
	@Column()
	private String main_image;	
	@Column()
	private String title;
	@Column()
	private String description ;
	
	
	@Builder
	public Meeting(int owner, String room_code, int num_people, String main_image, String title, String description) {
		this.owner = owner ;
		this.room_code = room_code;
		this.num_people = num_people;
		this.main_image = main_image;
		this.title = title;
		this.description = description;

	}
}
