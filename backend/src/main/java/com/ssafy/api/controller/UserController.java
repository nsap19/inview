package com.ssafy.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.api.request.UserLoginPostReq;
import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.api.response.UserLoginPostRes;
import com.ssafy.api.response.UserRes;
import com.ssafy.api.service.UserService;
import com.ssafy.common.auth.SsafyUserDetails;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.common.util.JwtTokenUtil;
import com.ssafy.db.entity.User;
import com.ssafy.db.repository.UserRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 유저 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
@Api(value = "유저 API", tags = {"User"})
@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	UserService userService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@PostMapping("/signup")
	@ApiOperation(value = "회원 가입", notes = "<strong>email과 password</strong>를 통해 회원가입 한다.") 
    @ApiResponses({
        @ApiResponse(code = 200, message = "회원가입 성공"),
        @ApiResponse(code = 401, message = "로그인이 필요한 페이지입니다."),
        @ApiResponse(code = 403, message = "접근 권한이 없습니다."),
        @ApiResponse(code = 404, message = "회원가입 실패")
    })
	public ResponseEntity<? extends BaseResponseBody> register(
			@RequestBody @ApiParam(value="회원가입 정보", required = true) UserRegisterPostReq registerInfo) {
		
		//임의로 리턴된 User 인스턴스. 현재 코드는 회원 가입 성공 여부만 판단하기 때문에 굳이 Insert 된 유저 정보를 응답하지 않음.
		String messageCreateUser = userService.createUser(registerInfo);
		if(messageCreateUser.equals("회원가입 성공"))
			return ResponseEntity.status(200).body(BaseResponseBody.of(200, messageCreateUser));
		else if(messageCreateUser.equals("이미 등록된 이메일입니다."))
			return ResponseEntity.status(409).body(BaseResponseBody.of(409, messageCreateUser));
		else if(messageCreateUser.equals("이미 등록된 닉네임입니다."))
			return ResponseEntity.status(409).body(BaseResponseBody.of(409, messageCreateUser));
		else
			return ResponseEntity.status(404).body(BaseResponseBody.of(404, "회원가입 실패"));
	}
	
	@PostMapping("/login")
	@ApiOperation(value = "로그인", notes="<strong>email과 password</strong>로 로그인 한다.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "로그인 성공"),
        @ApiResponse(code = 401, message = "로그인이 필요한 페이지입니다."),
        @ApiResponse(code = 403, message = "접근 권한이 없습니다."),
        @ApiResponse(code = 409, message = "잘못된 이메일 혹은 비밀번호")
	})
	public ResponseEntity<UserLoginPostRes> login(@RequestBody @ApiParam(value="로그인 정보", required = true) UserLoginPostReq loginInfo) {
		String email = loginInfo.getEmail();
		String password = loginInfo.getPassword();
		
		User user = userService.getUserByEmail(email);
		// 로그인 요청한 유저로부터 입력된 패스워드 와 디비에 저장된 유저의 암호화된 패스워드가 같은지 확인.(유효한 패스워드인지 여부 확인)
		if(passwordEncoder.matches(password, user.getPassword())) {
			// 유효한 패스워드가 맞는 경우, 로그인 성공으로 응답.(액세스 토큰을 포함하여 응답값 전달)
			return ResponseEntity.ok(UserLoginPostRes.of(200, "Success", JwtTokenUtil.getToken(email)));
		}
		// 유효하지 않는 패스워드인 경우, 로그인 실패로 응답.
		return ResponseEntity.status(401).body(UserLoginPostRes.of(401, "Invalid Password", null));
	}
	
	@GetMapping("/me")
	@ApiOperation(value = "회원 본인 정보 조회", notes = "로그인한 회원 본인의 정보를 응답한다.") 
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 401, message = "인증 실패"),
        @ApiResponse(code = 404, message = "사용자 없음"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
	public ResponseEntity<UserRes> getUserInfo(@ApiIgnore Authentication authentication) {
		/**
		 * 요청 헤더 액세스 토큰이 포함된 경우에만 실행되는 인증 처리이후, 리턴되는 인증 정보 객체(authentication) 통해서 요청한 유저 식별.
		 * 액세스 토큰이 없이 요청하는 경우, 403 에러({"error": "Forbidden", "message": "Access Denied"}) 발생.
		 */
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		String email = userDetails.getUsername();
		User user = userService.getUserByEmail(email);
		
		return ResponseEntity.status(200).body(UserRes.of(user));
	}
	
	@DeleteMapping("/{userId}")
	@ApiOperation(value = "회원 탈퇴", notes = "로그인한 회원의 정보가 db에서 삭제")
	@ApiResponses({
		@ApiResponse(code = 200, message = "유저 탈퇴 성공"),
        @ApiResponse(code = 401, message = "로그인이 필요한 페이지입니다."),
        @ApiResponse(code = 403, message = "접근 권한이 없습니다."),
        @ApiResponse(code = 404, message = "존재하지 않는 유저입니다.")
	})
	public void deleteUser(@PathVariable("userId") int user_id) {
		userService.deleteUser(user_id);
	}
}
