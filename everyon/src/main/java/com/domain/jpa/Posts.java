package com.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.app.dto.UpdateMeetDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
@Entity
@Table(name="posts")
public class Posts {

	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="meet_id")
    private Long meetId;
    @Column()
    private String meet_url;
    @Column()
    private String open_url;
    
	@Builder
	public Posts(Long meet_id, String meet_url, String open_url) {
		this.meetId = meet_id;
		this.meet_url = meet_url;
		this.open_url = open_url;
		
	}
	
	public void updateInfo(UpdateMeetDto putMeetDto) {
		
		if(putMeetDto.getMeet_url() != null)
		    this.meet_url = putMeetDto.getMeet_url();
		if(putMeetDto.getOpen_url() != null)
			this.open_url = putMeetDto.getOpen_url();
	}
}
