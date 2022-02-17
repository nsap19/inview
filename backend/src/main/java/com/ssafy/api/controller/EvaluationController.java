package com.ssafy.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.api.service.EvaluationService;
import com.ssafy.common.model.response.AdvancedResponseBody;
import com.ssafy.common.model.response.BaseResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "평가지 API", tags = { "Evaluation" })
@RestController
@RequestMapping("/evaluation")
public class EvaluationController {
	@Autowired
	EvaluationService evaluationService;

	@GetMapping
	@ApiOperation(value = "기본 면접 평가지 제공")
	@ApiResponses({ @ApiResponse(code = 200, message = "평가지 제공성공"), @ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<? extends BaseResponseBody> getEvaluation() {
		return ResponseEntity.status(200)
				.body(AdvancedResponseBody.of(200, "평가지 제공성공", evaluationService.getEvaluation()));
	}

//	@ApiModel
//	private class MeetingRegisterClass extends AdvancedResponseBody<MeetingRegisterRes> {
//	}
//
//	@ApiModel
//	private class MeetingJoinClass extends AdvancedResponseBody<MeetingJoinRes> {
//	}
}
