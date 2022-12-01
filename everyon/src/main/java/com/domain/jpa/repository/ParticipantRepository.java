package com.domain.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.domain.jpa.Participant;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long>{

	public List<Participant> findByMeetId(Long meet_id) ;
	
	public List<Participant> findByUserId(Long user_id) ;
}
