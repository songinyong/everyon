package com.domain.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.domain.jpa.MeetApplication;

@Repository
public interface MeetApplicationRepository extends JpaRepository<MeetApplication, Long>{

}
