package com.domain.jpa.log;

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
@Table(name="manage_log")
public class ManageUserLog extends CreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long meetId;
    private Long userId;
    private int status_code;

    
	@Builder
	public ManageUserLog(Long meet_id, Long userId, int status_code) {
		this.meetId = meet_id;
		this.userId = userId;
		this.status_code = status_code;
		
	}
    
}
