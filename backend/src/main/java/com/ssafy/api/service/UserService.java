package com.ssafy.api.service;

import com.ssafy.api.request.UserFindPwPostReq;
import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.api.request.VerifyCodePostReq;
import com.ssafy.db.entity.User;

/**
 *	유저 관련 비즈니스 로직 처리를 위한 서비스 인터페이스 정의.
 */
public interface UserService {
	void createUser(UserRegisterPostReq userRegisterInfo);
	void verifyCode(VerifyCodePostReq verifyCodeInfo);
	User getUserByUserId(int userId);
	User getUserByEmail(String email);
	User getUserByNickname(String nickname);
	User getUserById(int userId);
	void deleteUser(int userId);
	void findUser(UserFindPwPostReq userFindInfo);
}
