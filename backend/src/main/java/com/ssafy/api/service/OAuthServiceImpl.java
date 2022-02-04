package com.ssafy.api.service;

import java.time.LocalTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.api.response.Response;
import com.ssafy.api.response.UserLoginPostRes;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.common.util.JwtTokenUtil;
import com.ssafy.common.util.SaltGenerator;
import com.ssafy.db.entity.KakaoProfile;
import com.ssafy.db.entity.OAuthToken;
import com.ssafy.db.entity.User;
import com.ssafy.db.repository.UserRepository;

@Service
public class OAuthServiceImpl implements OAuthService {
	private static final String KAKAO_CLIENT_ID = "64d19023c74cf30131d34d087002a4dd";
	private static final String KAKAO_REDIRECT_URI = "http://localhost:8080/oauth/kakao";

	@Autowired
	private UserService userService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public ResponseEntity<String> getKakaoToken(String code) {
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", KAKAO_CLIENT_ID);
		params.add("redirect_uri", KAKAO_REDIRECT_URI);
		params.add("code", code);

		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = rt.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST,
				kakaoTokenRequest, String.class);

		return response;
	}

	@Override
	public ResponseEntity<String> getKakaoUserData(OAuthToken oauthToken) {
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + oauthToken.getAccessToken());
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);
		ResponseEntity<String> response = rt.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST,
				kakaoProfileRequest, String.class);

		return response;
	}

	@Override
	public Response registerAndLogin(KakaoProfile kakaoProfile) {
		UserRegisterPostReq registerInfo = new UserRegisterPostReq();

		// 이메일 보유시 이메일 조회
		if (kakaoProfile.getKakaoAccount().getHasEmail() && kakaoProfile.getKakaoAccount().getIsEmailVerified()) {
			String email = kakaoProfile.getKakaoAccount().getEmail();
			if (userService.getUserByEmail(email) != null) {
				// 기존 유저인 경우, 로그인 처리
				Response response = new Response(ResponseEntity.ok(UserLoginPostRes.of(200, "로그인 성공")).getStatusCode());
				response.add("token", JwtTokenUtil.getToken(email));
				return response;
			} else {
				// 이메일 정보 저장
				registerInfo.setEmail(email);
			}
		}else {
			registerInfo.setEmail(UUID.randomUUID().toString());
		}

		// 임시 유저 가입 & 로그인 처리
		String salt = new SaltGenerator(LocalTime.now().toString()).toString();
		String nickname = "kakao_" + salt + "_" + kakaoProfile.getProperties().getNickname();
		registerInfo.setNickname(nickname);
		registerInfo.setPassword(salt);
		ResponseEntity<? extends BaseResponseBody> result = createUser(registerInfo);
		if (!result.getStatusCode().toString().startsWith("200")) {
			return new Response(result.getStatusCode());
		}

		Response response = new Response(result.getStatusCode());
		if (registerInfo.getEmail() != null) {
			response.add("token", JwtTokenUtil.getToken(registerInfo.getEmail()));
		} else {
			response.add("token", JwtTokenUtil.getToken(nickname));
		}
		return response;
	}

	public ResponseEntity<? extends BaseResponseBody> createUser(UserRegisterPostReq userRegisterInfo) {
		if (userService.getUserByNickname(userRegisterInfo.getNickname()) != null) // 닉네임 중복 검사
			return ResponseEntity.status(409).body(BaseResponseBody.of(409, "이미 등록된 닉네임입니다.\n 카카오 회원가입/ 로그인 실패"));

		User user = new User();
		if (userRegisterInfo.getEmail() != null)
			user.setEmail(userRegisterInfo.getEmail());
		user.setNickname(userRegisterInfo.getNickname());
		user.setPassword(passwordEncoder.encode(userRegisterInfo.getPassword())); // 패스워드 암호화하여 db에 저장

		userRepository.save(user);
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "회원가입 성공"));
	}
}
