package com.ssafy.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.db.entity.MeetingCompany;

@Repository
public interface MeetingCompanyRepository extends JpaRepository<MeetingCompany, Integer> {
}