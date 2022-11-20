package com.domain.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.domain.jpa.Meeting;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface MeetRepository extends JpaRepository<Meeting, Long>  {

	   //@Query("SELECT s FROM Store s WHERE s.code_nm =?1")
	   //List<Store> findByCateory(String code_nm);
	   
		//@Query("SELECT s FROM Store s WHERE s.store_nm like %?1%")
	    //List<Store> findStoreByKeyword(String keyword);
	
	    Page<Meeting> findMeetingByCategory(Pageable pageable, String category);
	    
		@Query("SELECT m FROM Meeting m WHERE m.title like %?1%")
		Page<Meeting> findStoreByKeyword(Pageable pageable,String keyword);

		@Query("SELECT m FROM Meeting m WHERE m.title like %?1% and m.category = ?2")
		Page<Meeting> findStoreByKeywordAndCategory(Pageable pageable, String keyword, String category_code);
}
 