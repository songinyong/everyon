package com.domain.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.domain.jpa.CustomUser;

@Repository
public interface UserRepository extends JpaRepository<CustomUser, Long> {
	
	public CustomUser findByUid(String uid);
	
	public Optional<CustomUser> findByUidAndPlatform(String uid, String flatform);
}