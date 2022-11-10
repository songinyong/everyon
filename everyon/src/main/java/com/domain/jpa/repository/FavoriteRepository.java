package com.domain.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.domain.jpa.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long>{

	
}
