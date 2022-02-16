package com.ssafy.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.db.entity.Evaluation;
import com.ssafy.db.repository.EvaluationRepository;

@Service("evaluationService")
public class EvaluationServiceImpl implements EvaluationService {

	@Autowired
	EvaluationRepository evaluationRepository;

	@Override
	public List<Evaluation> getEvaluation() {
		return evaluationRepository.findAll();
	}
}
