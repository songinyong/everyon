package com.domain.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.domain.jpa.Participant;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long>{

	public List<Participant> findByMeetId(Long meet_id) ;
	
	public List<Participant> findByUserId(Long user_id) ;
	
	public Optional<Participant> findByUserIdAndMeetId(Long user_id, Long meet_id) ;
	
	public void deleteAllByMeetId(Long meet_id);
	
	public void deleteByUserIdAndMeetId(Long user_id, Long meet_id);
}
