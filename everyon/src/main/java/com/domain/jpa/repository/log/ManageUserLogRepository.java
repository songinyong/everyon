package com.domain.jpa.repository.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.domain.jpa.log.ManageUserLog;
@Repository
public interface ManageUserLogRepository extends JpaRepository<ManageUserLog, Long> {

}
