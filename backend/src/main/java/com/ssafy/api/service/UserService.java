package com.ssafy.api.service;

import org.springframework.http.ResponseEntity;

import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.api.request.UserUpdatePutReq;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.db.entity.User;

/**
 *	유저 관련 비즈니스 로직 처리를 위한 서비스 인터페이스 정의.
 */
public interface UserService {
	ResponseEntity<? extends BaseResponseBody> createUser(UserRegisterPostReq userRegisterInfo);
	User getUserByUserId(int userId);
	User getUserByEmail(String email);
	User getUserByNickname(String nickname);
	ResponseEntity<? extends BaseResponseBody> deleteUser(int userId);
	ResponseEntity<? extends BaseResponseBody> modifyUser(int userId, UserUpdatePutReq userUpdateInfo);
}
