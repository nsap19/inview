package com.ssafy.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.api.request.UserLoginPostReq;
import com.ssafy.api.request.UserLogoutGetReq;
import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.api.request.UserUpdatePutReq;
import com.ssafy.api.request.VerifyCodePostReq;
import com.ssafy.api.response.Response;
import com.ssafy.api.response.UserLoginPostRes;
import com.ssafy.api.service.EmailService;
import com.ssafy.api.service.UserService;
import com.ssafy.common.model.response.AdvancedResponseBody;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.common.util.JwtTokenUtil;
import com.ssafy.db.entity.User;
import com.ssafy.db.repository.UserRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

/**
 * 유저 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
@Api(value = "유저 API", tags = {"User"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/signup")
	@ApiOperation(value = "회원 가입", notes = "<strong>email과 password</strong>를 통해 회원가입 한다.") 
    @ApiResponses({
        @ApiResponse(code = 200, message = "회원가입 성공"),
        @ApiResponse(code = 400, message = "회원가입 실패"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
	public Response register(@RequestBody UserRegisterPostReq registerInfo) {
		ResponseEntity<? extends BaseResponseBody> result = userService.createUser(registerInfo);
		return new Response(result.getStatusCode());
	} 
	
	@PostMapping("/signup/email-certi") 
	@ApiOperation(value = "이메일 인증")
	@ApiResponses({
    	@ApiResponse(code = 200, message = "이메일 인증 성공"),
    	@ApiResponse(code = 400, message = "존재하지 않는 이메일 인증 정보입니다."),
        @ApiResponse(code = 500, message = "서버 오류")
    })
    public Response verifyCode(@RequestBody VerifyCodePostReq verifyCodeInfo) {
        if(EmailService.ePw.equals(verifyCodeInfo.getCode().get("code"))) {
        	return new Response(ResponseEntity.status(200).body(BaseResponseBody.of(200, "이메일 인증 코드 검증 성공")).getStatusCode());
        }
        else{
        	return new Response(ResponseEntity.status(200).body(BaseResponseBody.of(401, "이메일 인증 코드 검증 실패")).getStatusCode());
        }
    }
	
	@PostMapping("/login")
	@ApiOperation(value = "로그인", notes="<strong>email과 password</strong>로 로그인 한다.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "로그인 성공"),
        @ApiResponse(code = 400, message = "잘못된 이메일 혹은 비밀번호"),
        @ApiResponse(code = 500, message = "서버 오류")
	})
	public Response login(@RequestBody UserLoginPostReq loginInfo) throws Exception {
		String email = loginInfo.getEmail();
		String password = loginInfo.getPassword();
		
		if(userService.getUserByEmail(email) == null)
			return new Response(ResponseEntity.status(401).body(UserLoginPostRes.of(401, "잘못된 이메일")).getStatusCode());

		User user = userService.getUserByEmail(email);
		
		if(!passwordEncoder.matches(password, user.getPassword())) // 패스워드 일치 확인
			return new Response(ResponseEntity.status(401).body(UserLoginPostRes.of(401, "잘못된 비밀번호")).getStatusCode());
		
		Response response = new Response(ResponseEntity.ok(UserLoginPostRes.of(200, "로그인 성공")).getStatusCode());
		response.add("token", JwtTokenUtil.getToken(email));
		
		return response;
	}
	
	@GetMapping("/logout")
	@ApiOperation(value = "로그아웃")
	@ApiResponses({
		@ApiResponse(code = 200, message = "로그아웃 성공"),
		@ApiResponse(code = 400, message = "로그아웃 실패"),
        @ApiResponse(code = 500, message = "서버 오류")
	})
	public Response logout(@RequestBody UserLogoutGetReq logoutInfo) {
		HttpServletRequest request = logoutInfo.getRequset();
		HttpServletResponse response = logoutInfo.getResponse();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		new SecurityContextLogoutHandler().logout(request, response, auth);
		
		return new Response();
	}
	
	@DeleteMapping("/{userId}")
	@ApiImplicitParam(name = "userId", value ="유저 아이디")
	@ApiOperation(value = "회원 탈퇴", notes = "로그인한 회원의 정보가 db에서 삭제")
	@ApiResponses({
		@ApiResponse(code = 200, message = "유저 탈퇴 성공"),
        @ApiResponse(code = 400, message = "존재하지 않는 유저입니다."),
        @ApiResponse(code = 500, message = "서버 오류")
	})
	public Response deleteUser(@PathVariable("userId") int userId) {
		ResponseEntity<? extends BaseResponseBody> result = userService.deleteUser(userId);
		return new Response(result.getStatusCode());
	}
}