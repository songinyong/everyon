package com.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name="participant")
public class Participant {
	
	@Id
    private Long userId;
    
	@Column()
	private Long meetId ;
	
	@Builder
	public Participant(Long user_id, Long meet_id) {
		this.userId = user_id;
		this.meetId = meet_id;
	}

}
