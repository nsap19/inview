package com.ssafy.api.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.api.request.UserFindPwPostReq;
import com.ssafy.api.request.UserIssuePwPostReq;
import com.ssafy.api.request.UserLoginPostReq;
import com.ssafy.api.request.UserLogoutGetReq;
import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.api.request.VerifyCodePostReq;
import com.ssafy.api.service.UserService;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.common.model.response.CodeResponseBody;
import com.ssafy.common.model.response.TokenResponseBody;
import com.ssafy.common.util.JwtTokenUtil;
import com.ssafy.db.entity.User;
import com.ssafy.db.repository.UserRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 유저 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
@Api(value = "유저 API", tags = {"User"})
//@RequiredArgsConstructor
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
	@ApiOperation(value = "회원 가입", notes = "<strong>email과 password, nickname</strong>을 통해 회원가입 해야 합니다.\n"
			+ "email은 형식을 준수해야 하고, 비밀번호는<strong> 8자리 이상</strong>이어야 합니다.") 
    @ApiResponses({
        @ApiResponse(code = 200, message = "이메일 인증 전송 성공"),
        @ApiResponse(code = 400, message = "회원가입 실패"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
	public ResponseEntity<? extends BaseResponseBody> register(@RequestBody UserRegisterPostReq registerInfo) {
		if(userService.getUserByEmail(registerInfo.getEmail()) != null) // 이메일 중복 검사
			return ResponseEntity.status(409).body(BaseResponseBody.of(409, "이미 등록된 이메일입니다."));
		if(userService.getUserByNickname(registerInfo.getNickname()) != null) // 닉네임 중복 검사
			return ResponseEntity.status(409).body(BaseResponseBody.of(409, "이미 등록된 닉네임입니다."));
		
		String regx = "^[A-Za-z0-9+_.-]+@(.+)$";
		Pattern pattern = Pattern.compile(regx);
		Matcher matcher = pattern.matcher(registerInfo.getEmail());
		if(!matcher.matches()) // 이메일 형식 유효성 검사
			return ResponseEntity.status(409).body(BaseResponseBody.of(409, "이메일 형식이 올바르지 않습니다."));
		if(registerInfo.getPassword() == null || registerInfo.getPassword().length() < 8) // 비밀번호 유효성 검사
			return ResponseEntity.status(409).body(BaseResponseBody.of(409, "비밀번호는 8글자 이상이어야 합니다."));
		
		String code = userService.createUser(registerInfo);
		
		return ResponseEntity.status(200).body(CodeResponseBody.of(200, "이메일, 닉네임, 비밀번호 유효성 검사 성공. 인증번호 전송 완료", code));
	} 
	
	@PostMapping("/signup/email-certi") 
	@ApiOperation(value = "이메일 인증")
	@ApiResponses({
    	@ApiResponse(code = 200, message = "이메일 인증 성공"),
    	@ApiResponse(code = 400, message = "존재하지 않는 이메일 인증 정보입니다."),
        @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<? extends BaseResponseBody> verifyCode(@RequestBody VerifyCodePostReq verifyCodeInfo) {
        userService.verifyCode(verifyCodeInfo);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "이메일 인증 코드 검증 성공"));
    }
	
	@PostMapping("/login")
	@ApiOperation(value = "로그인", notes="<strong>email과 password</strong>로 로그인 한다.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "로그인 성공"),
        @ApiResponse(code = 400, message = "잘못된 이메일 혹은 비밀번호"),
        @ApiResponse(code = 500, message = "서버 오류")
	})
	public ResponseEntity<? extends BaseResponseBody> login(@RequestBody UserLoginPostReq loginInfo) throws Exception {
		String email = loginInfo.getEmail();
		String password = loginInfo.getPassword();
		
		if(userService.getUserByEmail(email) == null)
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "잘못된 이메일"));

		User user = userService.getUserByEmail(email);
		
		if(!passwordEncoder.matches(password, user.getPassword())) // 패스워드 일치 확인
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "잘못된 비밀번호"));
		
		return ResponseEntity.status(200).body(TokenResponseBody.of(200, "로그인 성공", user.getUserId(), user.getNickname(),JwtTokenUtil.getToken(email)));
	}
	
	@GetMapping("/logout")
	@ApiOperation(value = "로그아웃")
	@ApiResponses({
		@ApiResponse(code = 200, message = "로그아웃 성공"),
		@ApiResponse(code = 400, message = "로그아웃 실패"),
        @ApiResponse(code = 500, message = "서버 오류")
	})
	public ResponseEntity<? extends BaseResponseBody> logout(@RequestBody UserLogoutGetReq logoutInfo) {
		HttpServletRequest request = logoutInfo.getRequset();
		HttpServletResponse response = logoutInfo.getResponse();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		new SecurityContextLogoutHandler().logout(request, response, auth);
		
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "로그아웃 성공"));
	}
	
	@PostMapping("/findpw")
	@ApiOperation(value="비밀번호 찾기")
	@ApiResponses({
		@ApiResponse(code = 200, message = "이메일 인증 전송 성공"),
		@ApiResponse(code = 400, message = "존재하지 않는 이메일 인증 정보입니다."),
		@ApiResponse(code = 500, message = "서버 오류")
	})
	public ResponseEntity<? extends BaseResponseBody> findPw(@RequestBody UserFindPwPostReq findInfo) {
		if(userService.getUserByEmail(findInfo.getEmail()) == null) // 이메일 중복 검사
			return ResponseEntity.status(409).body(BaseResponseBody.of(409, "존재하지 않는 이메일 입니다."));

		String code = userService.findUser(findInfo);
		return ResponseEntity.status(200).body(CodeResponseBody.of(200, "이메일 인증 전송 성공", code));
	}
	
	@PostMapping("/findpw/email-certi")
	@ApiOperation(value="임시 비밀번호 발급")
	@ApiResponses({
		@ApiResponse(code = 200, message = "임시 비밀번호 발급 성공"),
		@ApiResponse(code = 400, message = "임시 비밀번호 발급 실패"),
		@ApiResponse(code = 500, message = "서버 오류")
	})
	public ResponseEntity<? extends BaseResponseBody> issuePw(@RequestBody UserIssuePwPostReq issuePwInfo) {
		userService.issuePassword(issuePwInfo);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "임시 비밀번호 발급 성공"));
	}
	
	@DeleteMapping("/{userId}")
	@ApiImplicitParam(name = "userId", value ="userId")
	@ApiOperation(value = "회원 탈퇴", notes = "로그인한 회원의 정보가 db에서 삭제")
	@ApiResponses({
		@ApiResponse(code = 200, message = "유저 탈퇴 성공"),
        @ApiResponse(code = 400, message = "존재하지 않는 유저입니다."),
        @ApiResponse(code = 500, message = "서버 오류")
	})
	public ResponseEntity<? extends BaseResponseBody> deleteUser(@PathVariable("userId") int userId, @RequestParam("password") String password) {
		if(!passwordEncoder.matches(password, userService.getUserById(userId).getPassword())) // 패스워드 일치 확인
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "잘못된 비밀번호"));
		userService.deleteUser(userId);
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "유저 탈퇴 성공"));
	}
}
