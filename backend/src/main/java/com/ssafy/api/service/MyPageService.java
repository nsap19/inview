package com.ssafy.api.service;

import com.ssafy.common.model.response.BaseResponseBody;

import org.springframework.http.ResponseEntity;

import com.ssafy.api.request.UserUpdatePutReq;

public interface MyPageService {
	ResponseEntity<? extends BaseResponseBody> modifyUser(int userId, UserUpdatePutReq userUpdateInfo);
}