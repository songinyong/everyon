package com.domain.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.domain.jpa.Posts;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {
	
	public Optional<Posts> findByMeetId(Long meet_id);

}
