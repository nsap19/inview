package com.ssafy.api.controller;

import java.time.LocalTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.ssafy.api.request.UserLoginPostReq;
import com.ssafy.api.request.UserLogoutGetReq;
import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.api.request.VerifyCodePostReq;
import com.ssafy.api.response.Response;
import com.ssafy.api.response.UserLoginPostRes;
import com.ssafy.api.service.OAuthService;
import com.ssafy.api.service.UserService;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.common.util.JwtTokenUtil;
import com.ssafy.common.util.SaltGenerator;
import com.ssafy.db.entity.KakaoProfile;
import com.ssafy.db.entity.OAuthToken;
import com.ssafy.db.entity.User;
import com.ssafy.db.repository.UserRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

/**
 * 소셜 로그인 유저 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
@Api(value = "소셜 유저 API", tags = { "oauth" })
@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth")
public class OAuthController {
	@Autowired
	private OAuthService oauthService;

	@GetMapping("/kakao")
	@ApiOperation(value = "카카오 회원가입/로그인 callback 함수", notes = "<strong>카카오 로그인</strong>을 통해 회원가입 한다.")
	@ApiResponses({ @ApiResponse(code = 200, message = "회원가입/ 로그인 성공"), 
			@ApiResponse(code = 400, message = "회원가입/ 로그인 실패"),
			@ApiResponse(code = 500, message = "서버 오류") })
	public Response kakaoCallback(@RequestParam String code) {
		System.out.println("카카오 인증 완료/ 인가 코드 : " + code);
		// 인가 코드로 토큰 발급받기
		ResponseEntity<String> responseEntity = oauthService.getKakaoToken(code);

		// json 데이터를 담기 위한 Object 객체
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			OAuthToken oauthToken = objectMapper.readValue(responseEntity.getBody(), OAuthToken.class);
			responseEntity = oauthService.getKakaoUserData(oauthToken);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return new Response(ResponseEntity.status(400).body(BaseResponseBody.of(400, "카카오 인증 실패")).getStatusCode());
		}

		KakaoProfile kakaoProfile = new Gson().fromJson(responseEntity.getBody(), KakaoProfile.class);
		Response response = oauthService.registerAndLogin(kakaoProfile);

		return response;
	}

	@PostMapping("/google")
	@ApiOperation(value = "구글 회원가입/로그인 callback 함수", notes = "<strong>구글 로그인</strong>을 통해 회원가입 한다.")
	@ApiResponses({ @ApiResponse(code = 200, message = "회원가입/ 로그인 성공"), @ApiResponse(code = 400, message = "로그인 실패"),
			@ApiResponse(code = 500, message = "서버 오류") })
	public Response googleCallback(@RequestBody VerifyCodePostReq verifyCodeInfo) {
		//미구현
		return new Response();
	}

	@PostMapping("/kakao/logout")
	@ApiOperation(value = "로그아웃")
	@ApiResponses({ @ApiResponse(code = 200, message = "로그아웃 성공"), @ApiResponse(code = 400, message = "로그아웃 실패"),
			@ApiResponse(code = 500, message = "서버 오류") })
	public Response kakaoLogout(@RequestBody UserLogoutGetReq logoutInfo) throws Exception {
		//미구현
		return new Response();
	}

	@GetMapping("/google/logout")
	@ApiOperation(value = "로그아웃")
	@ApiResponses({ @ApiResponse(code = 200, message = "로그아웃 성공"), @ApiResponse(code = 400, message = "로그아웃 실패"),
			@ApiResponse(code = 500, message = "서버 오류") })
	public Response googlelogout(@RequestBody UserLogoutGetReq logoutInfo) {
		//미구현
		return new Response();
	}
}
