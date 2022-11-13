package com.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name="favorite")
public class Favorite {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name="user_id")
    private Long userId;
    
	@Column(name="meet_id")
	private Long meetId ;
	
	@Builder
	public Favorite(Long user_id, Long meet_id) {
		this.userId = user_id;
		this.meetId = meet_id;
	}

}
