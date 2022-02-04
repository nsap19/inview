package com.ssafy.api.service;

import org.springframework.http.ResponseEntity;

import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.api.response.Response;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.db.entity.KakaoProfile;
import com.ssafy.db.entity.OAuthToken;

public interface OAuthService {
	ResponseEntity<String> getKakaoToken(String code);
	ResponseEntity<String> getKakaoUserData(OAuthToken oauthToken);
	ResponseEntity<? extends BaseResponseBody> createUser(UserRegisterPostReq userRegisterInfo);
	Response registerAndLogin(KakaoProfile kakaoProfile);
}
