package com.ssafy.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.db.entity.Industry;

@Repository
public interface IndustryRepository extends JpaRepository<Industry, Integer> {
	Optional<Industry> findByIndustryName(String industryName);
}