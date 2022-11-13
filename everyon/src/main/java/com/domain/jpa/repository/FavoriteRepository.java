package com.domain.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.domain.jpa.Favorite;
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long>{

	List<Favorite> findAllByUserId(Long user_id);
	
	void deleteByUserIdAndMeetId(Long user_id, Long meet_id);
}
