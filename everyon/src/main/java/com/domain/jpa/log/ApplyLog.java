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
@Table(name="apply_log")
public class ApplyLog extends CreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="meet_id")
    private Long meetId;
    private Long userId;
    private boolean appr;
    private String refusalDec;
    
	@Builder
	public ApplyLog(Long meet_id, Long userId, boolean appr, String refusalDec) {
		this.meetId = meet_id;
		this.userId = userId;
		this.appr = appr;
		this.refusalDec = refusalDec;
		
	}
    
}
