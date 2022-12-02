package com.domain.jpa.repository.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.domain.jpa.ApplyLog;


@Repository
public interface ApplyLogRepository extends JpaRepository<ApplyLog, Long>{

}
