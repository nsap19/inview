package com.ssafy.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.api.request.UserUpdatePutReq;
import com.ssafy.api.service.MyPageService;
import com.ssafy.api.service.UserService;
import com.ssafy.common.model.response.AdvancedResponseBody;
import com.ssafy.common.model.response.BaseResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@Api(value = "마이페이지 API", tags = {"MyPage"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class MyPageController {
	
	@Autowired
	private MyPageService myPageService;
	private UserService userService;
	
	@PutMapping("/{userId}")
	@ApiImplicitParam(name = "userId", value ="userId")
	@ApiOperation(value = "회원 정보 수정", notes = "수정한 회원 정보로 db 업데이트")
	@ApiResponses({
		@ApiResponse(code = 200, message = "회원 정보 수정 성공"),
        @ApiResponse(code = 400, message = "존재하지 않는 유저입니다."),
        @ApiResponse(code = 500, message = "서버 오류")
	})
	public ResponseEntity<? extends BaseResponseBody> modifyUser(@PathVariable("userId") int userId, @RequestBody UserUpdatePutReq updateInfo) {
		if (userService.getUserByUserId(userId).getNickname() != updateInfo.getNickname() // 닉네임을 변경하는 경우
			&& userService.getUserByNickname(updateInfo.getNickname()) != null) // 닉네임 중복 검사
				return ResponseEntity.status(409).body(BaseResponseBody.of(409, "이미 등록된 닉네임입니다."));
		
		myPageService.modifyUser(userId, updateInfo);
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "회원 정보 수정 성공"));
	}
	
	@GetMapping("/{userId}/meeting")
	@ApiImplicitParam(name = "userId", value = "userId")
	@ApiOperation(value = "지난 미팅 내역 전체 조회")
	@ApiResponses({
		@ApiResponse(code = 200, message = "지난 미팅 내역 전체 조회 성공"),
		@ApiResponse(code = 400, message = "존재하지 않는 유저입니다."),
		@ApiResponse(code = 500, message = "서버 오류")
	})
	public ResponseEntity<? extends BaseResponseBody> searchAll(@PathVariable("userId") int userId) {
		return ResponseEntity.status(200)
				.body(AdvancedResponseBody.of(200, "지난 미팅 내역 전체 조회 성공", myPageService.searchMeeting(userId)));
	}

	@GetMapping("/{userId}/meeting/{meetingId}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "userId", value = "userId"),
		@ApiImplicitParam(name = "meetingId", value = "meetingId")
	})
	@ApiOperation(value = "지난 미팅 내역 상세 조회")
	@ApiResponses({
		@ApiResponse(code = 200, message = "지난 미팅 내역 상세 조회 성공"),
		@ApiResponse(code = 400, message = "존재하지 않는 유저입니다. \n 존재하지 않는 미팅입니다."),
		@ApiResponse(code = 500, message = "서버 오류")
	})
	public ResponseEntity<? extends BaseResponseBody> searchDetail(@PathVariable("userId") int userId, @PathVariable("meetingId") int meetingId) {
		return ResponseEntity.status(200)
				.body(AdvancedResponseBody.of(200, "지난 미팅 내역 상세 조회 성공", myPageService.searchMeetingDetail(userId, meetingId)));
	}
}