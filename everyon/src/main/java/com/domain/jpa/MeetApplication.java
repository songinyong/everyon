package com.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.domain.CreateTimeEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name="meet_application")
public class MeetApplication extends CreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name="user_id")
    private Long userId;
    
	@Column(name="meet_id")
	private Long meetId ;
	
	@Column()
	private String description ;
	
	@Builder
	public MeetApplication(Long user_id, Long meet_id, String description) {
		this.userId = user_id;
		this.meetId = meet_id;
		this.description = description;
	}
	
	
}
