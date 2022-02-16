package com.ssafy.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.api.request.UserUpdatePutReq;
import com.ssafy.api.response.LastMeetingRes;
import com.ssafy.api.service.MyPageService;
import com.ssafy.api.service.UserService;
import com.ssafy.common.model.response.AdvancedResponseBody;
import com.ssafy.common.model.response.BaseResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@Api(value = "마이페이지 API", tags = { "MyPage" })
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class MyPageController {

	@Autowired
	private MyPageService myPageService;
	@Autowired
	private UserService userService;

	@PutMapping("/{userId}")
	@ApiImplicitParam(name = "userId", value = "userId")
	@ApiOperation(value = "회원 정보 수정", notes = "이메일은 변경할 수 없고, 닉네임 또는 비밀번호를 변경할 수 있습니다.")
	@ApiResponses({ @ApiResponse(code = 200, message = "회원 정보 수정 성공"),
			@ApiResponse(code = 400, message = "존재하지 않는 유저입니다."), @ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<? extends BaseResponseBody> modifyUser(@PathVariable("userId") int userId,
			@RequestBody UserUpdatePutReq updateInfo) {

		if (!userService.getUserByUserId(userId).getNickname().equals(updateInfo.getNickname()) // 닉네임을 변경하는 경우
				&& userService.getUserByNickname(updateInfo.getNickname()) != null) // 닉네임 중복 검사
			return ResponseEntity.status(409).body(BaseResponseBody.of(409, "이미 등록된 닉네임입니다."));

		myPageService.modifyUser(userId, updateInfo);
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "회원 정보 수정 성공"));
	}
	
	@GetMapping("/{userId}/futureMeeting")
	@ApiImplicitParam(name = "userId", value = "userId")
	@ApiOperation(value = "참가할 미팅 내역 전체 조회", response = LastMeetingResClass.class)
	@ApiResponses({ @ApiResponse(code = 200, message = "참가할 미팅 내역 전체 조회 성공"),
			@ApiResponse(code = 400, message = "존재하지 않는 유저입니다."), @ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<? extends BaseResponseBody> searchFutureAll(@PathVariable("userId") int userId) {
		return ResponseEntity.status(200)
				.body(AdvancedResponseBody.of(200, "참가할 미팅 내역 전체 조회 성공", myPageService.searchFutureMeeting(userId)));
	}

	@GetMapping("/{userId}/meeting")
	@ApiImplicitParam(name = "userId", value = "userId")
	@ApiOperation(value = "지난 미팅 내역 전체 조회", response = LastMeetingResClass.class)
	@ApiResponses({ @ApiResponse(code = 200, message = "지난 미팅 내역 전체 조회 성공"),
			@ApiResponse(code = 400, message = "존재하지 않는 유저입니다."), @ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<? extends BaseResponseBody> searchAll(@PathVariable("userId") int userId,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		return ResponseEntity.status(200)
				.body(AdvancedResponseBody.of(200, "지난 미팅 내역 전체 조회 성공", myPageService.searchMeeting(userId, page)));
	}

	@GetMapping("/{userId}/meeting/{meetingId}")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "userId"),
			@ApiImplicitParam(name = "meetingId", value = "meetingId") })
	@ApiOperation(value = "지난 미팅 내역 상세 조회")
	@ApiResponses({ @ApiResponse(code = 200, message = "지난 미팅 내역 상세 조회 성공"),
			@ApiResponse(code = 400, message = "존재하지 않는 유저입니다. \n 존재하지 않는 미팅입니다."),
			@ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<? extends BaseResponseBody> searchDetail(@PathVariable("userId") int userId,
			@PathVariable("meetingId") int meetingId) {
		return ResponseEntity.status(200).body(AdvancedResponseBody.of(200, "지난 미팅 내역 상세 조회 성공",
				myPageService.searchMeetingDetail(userId, meetingId)));
	}

	@ApiModel
	private class LastMeetingResClass extends AdvancedResponseBody<Page<List<LastMeetingRes>>> {
	}
}