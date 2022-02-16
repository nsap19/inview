package com.ssafy.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.db.entity.Evaluation;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {

}