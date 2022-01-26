package com.ssafy.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ssafy.common.exception.handler.AlreadyFullParticipantException;
import com.ssafy.common.exception.handler.AlreadyJoinMeetingException;
import com.ssafy.common.exception.handler.AlreadyRunningMeetingException;
import com.ssafy.common.exception.handler.NotEqualPasswordException;
import com.ssafy.common.exception.handler.NotExistsCompanyException;
import com.ssafy.common.exception.handler.NotExistsIndustryException;
import com.ssafy.common.exception.handler.NotExistsMeetingException;
import com.ssafy.common.exception.handler.NotExistsUserException;
import com.ssafy.common.exception.handler.NotHostException;
import com.ssafy.common.model.response.BaseResponseBody;

@RestControllerAdvice
public class ExceptionAdvice {
	@ExceptionHandler(NotExistsUserException.class)
	public ResponseEntity<BaseResponseBody> handleNotExistsMemberException(NotExistsUserException e) {
		return ResponseEntity.status(400).body(BaseResponseBody.of(400, e.getMessage()));
	}

	@ExceptionHandler(NotExistsMeetingException.class)
	public ResponseEntity<BaseResponseBody> NotExistsMeetingException(NotExistsMeetingException e) {
		return ResponseEntity.status(400).body(BaseResponseBody.of(400, e.getMessage()));
	}

	@ExceptionHandler(NotExistsIndustryException.class)
	public ResponseEntity<BaseResponseBody> NotExistsIndustryException(NotExistsIndustryException e) {
		return ResponseEntity.status(400).body(BaseResponseBody.of(400, e.getMessage()));
	}

	@ExceptionHandler(NotExistsCompanyException.class)
	public ResponseEntity<BaseResponseBody> NotExistsCompanyException(NotExistsCompanyException e) {
		return ResponseEntity.status(400).body(BaseResponseBody.of(400, e.getMessage()));
	}

	@ExceptionHandler(NotHostException.class)
	public ResponseEntity<BaseResponseBody> NotHostException(NotHostException e) {
		return ResponseEntity.status(400).body(BaseResponseBody.of(400, e.getMessage()));
	}

	@ExceptionHandler(AlreadyRunningMeetingException.class)
	public ResponseEntity<BaseResponseBody> AlreadyRunningMeetingException(AlreadyRunningMeetingException e) {
		return ResponseEntity.status(400).body(BaseResponseBody.of(400, e.getMessage()));
	}

	@ExceptionHandler(NotEqualPasswordException.class)
	public ResponseEntity<BaseResponseBody> NotEqualPasswordException(NotEqualPasswordException e) {
		return ResponseEntity.status(400).body(BaseResponseBody.of(400, e.getMessage()));
	}

	@ExceptionHandler(AlreadyJoinMeetingException.class)
	public ResponseEntity<BaseResponseBody> AlreadyJoinMeetingException(AlreadyJoinMeetingException e) {
		return ResponseEntity.status(400).body(BaseResponseBody.of(400, e.getMessage()));
	}

	@ExceptionHandler(AlreadyFullParticipantException.class)
	public ResponseEntity<BaseResponseBody> AlreadyFullParticipantException(AlreadyFullParticipantException e) {
		return ResponseEntity.status(400).body(BaseResponseBody.of(400, e.getMessage()));
	}

}
