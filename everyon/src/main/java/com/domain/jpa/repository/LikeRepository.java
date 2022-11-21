package com.domain.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.domain.jpa.Like;
@Repository
public interface LikeRepository extends JpaRepository<Like, Long>{

	List<Like> findAllByUserId(Long user_id);
	
	Optional<Like> findByUserIdAndMeetId(Long user_id, Long meet_id);
	
	void deleteByUserIdAndMeetId(Long user_id, Long meet_id);
}
