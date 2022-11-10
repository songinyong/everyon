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
@Table(name="favorite")
public class Favorite {
	
	@Id
    private Long user_id;
    
	@Column()
	private Long meet_id ;
	
	@Builder
	public Favorite(Long user_id, Long meet_id) {
		this.user_id = user_id;
		this.meet_id = meet_id;
	}

}
