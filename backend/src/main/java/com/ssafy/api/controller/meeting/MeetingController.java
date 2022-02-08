package com.ssafy.api.controller.meeting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.api.request.meeting.MeetingRegisterPostReq;
import com.ssafy.api.response.MeetingJoinRes;
import com.ssafy.api.response.MeetingRegisterRes;
import com.ssafy.api.service.meeting.MeetingService;
import com.ssafy.common.model.response.AdvancedResponseBody;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.common.util.CurrentUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "미팅 API", tags = { "Meeting" })
@RestController
@RequestMapping("/meeting")
public class MeetingController {
	@Autowired
	MeetingService meetingService;

	@PostMapping
	@ApiOperation(value = "미팅 생성")
	@ApiResponses({ @ApiResponse(code = 200, message = "미팅 생성 성공", response = MeetingRegisterClass.class),
			@ApiResponse(code = 400, message = "존재하지 않는 유저입니다. \n 존재하지 않는 직군입니다. \n 존재하지 않는 기업명입니다."),
			@ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<? extends BaseResponseBody> register(
			@RequestBody @ApiParam(value = "미팅생성 정보", required = true) MeetingRegisterPostReq registerInfo) {

		return ResponseEntity.status(200).body(AdvancedResponseBody.of(200, "미팅 생성 성공",
				meetingService.createMeeting(registerInfo, CurrentUser.getUserId())));
	}

	@DeleteMapping("/{meetingId}")
	@ApiOperation(value = "미팅 소멸", notes = "미팅 시작 전 미팅실 삭제")
	@ApiResponses({ @ApiResponse(code = 200, message = "미팅 삭제 성공"),
			@ApiResponse(code = 400, message = "존재하지 않는 미팅입니다. \n 호스트가 아닙니다. \n 이미 시작된 미팅입니다."),
			@ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<? extends BaseResponseBody> delete(@PathVariable("meetingId") int meetingId) {

		meetingService.deleteMeeting(meetingId, CurrentUser.getUserId());

		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "미팅 삭제 성공"));
	}

	@PutMapping("/{meetingId}")
	@ApiOperation(value = "미팅 수정", notes = "미팅 제목 수정")
	@ApiResponses({ @ApiResponse(code = 200, message = "미팅 제목 수정 성공"),
			@ApiResponse(code = 400, message = "존재하지 않는 미팅입니다. \n 호스트가 아닙니다. "),
			@ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<? extends BaseResponseBody> modify(@PathVariable("meetingId") int meetingId, String title) {

		meetingService.updateMeeting(meetingId, title, CurrentUser.getUserId());

		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "미팅 제목 수정 성공"));
	}

	@PostMapping("/{meetingId}/join")
	@ApiOperation(value = "미팅 참가")
	@ApiResponses({ @ApiResponse(code = 200, message = "미팅 참가 신청 성공", response = MeetingJoinClass.class),
			@ApiResponse(code = 400, message = "존재하지 않는 미팅입니다. \n 이미 최대 참가자가 참여한 미팅입니다. \n 존재하지 않는 유저입니다. \n 이미 참가한 유저입니다. \n 비밀번호가 일치하지 않습니다."),
			@ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<? extends BaseResponseBody> join(@PathVariable("meetingId") int meetingId, String password) {

		return ResponseEntity.status(200).body(AdvancedResponseBody.of(200, "미팅 참가 신청 성공",
				meetingService.joinMeeting(meetingId, password, CurrentUser.getUserId())));
	}

	@ApiModel
	private class MeetingRegisterClass extends AdvancedResponseBody<MeetingRegisterRes> {
	}

	@ApiModel
	private class MeetingJoinClass extends AdvancedResponseBody<MeetingJoinRes> {
	}
}
