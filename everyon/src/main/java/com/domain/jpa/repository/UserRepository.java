package com.domain.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.domain.jpa.CustomUser;

@Repository
public interface UserRepository extends JpaRepository<CustomUser, String> {
	
	public CustomUser findByUid(String uid);
}