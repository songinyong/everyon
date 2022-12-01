package com.domain.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.domain.jpa.MeetApplication;
import com.domain.jpa.Participant;

@Repository
public interface MeetApplicationRepository extends JpaRepository<MeetApplication, Long>{

	public List<Optional<MeetApplication>> findByUserId(Long user_id) ;
	
	public List<Optional<MeetApplication>> findByUserIdAndMeetId(Long user_id, Long meet_id);
}
