package com.ssafy.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.db.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
	Optional<Company> findByCompanyName(String companyName);
}