package com.ssafy.api.controller.meeting;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.api.request.ArchiveRegisterPostReq;
import com.ssafy.api.request.meeting.MeetingRegisterPostReq;
import com.ssafy.api.service.ArchiveService;
import com.ssafy.api.service.meeting.MeetingService;
import com.ssafy.common.auth.SsafyUserDetails;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.common.util.MD5Generator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@Api(value = "미팅 API", tags = { "Meeting" })
@RestController
@RequestMapping("/meeting")
public class MeetingController {
	@Autowired
	MeetingService meetingService;

	@PostMapping
	@ApiOperation(value = "미팅 생성")
	@ApiResponses({ @ApiResponse(code = 200, message = "미팅 생성 성공"),
			@ApiResponse(code = 400, message = "존재하지 않는 유저입니다. \n 존재하지 않는 직군입니다. \n 존재하지 않는 기업명입니다."),
			@ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<? extends BaseResponseBody> register(
			@RequestBody @ApiParam(value = "미팅생성 정보", required = true) MeetingRegisterPostReq registerInfo,
			@AuthenticationPrincipal SsafyUserDetails ssafyUserDetails) {

		int hostId = ssafyUserDetails.getUser().getUserId();

		meetingService.createMeeting(registerInfo, hostId);

		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "미팅 생성 성공"));
	}

	@DeleteMapping("/{meetingId}")
	@ApiOperation(value = "미팅 소멸", notes = "미팅 시작 전 미팅실 삭제")
	@ApiResponses({ @ApiResponse(code = 200, message = "미팅 삭제 성공"),
			@ApiResponse(code = 400, message = "존재하지 않는 미팅입니다. \n 호스트가 아닙니다. \n 이미 시작된 미팅입니다."),
			@ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<? extends BaseResponseBody> delete(@PathVariable("meetingId") int meetingId,
			@ApiIgnore @AuthenticationPrincipal SsafyUserDetails ssafyUserDetails) {

		int hostId = ssafyUserDetails.getUser().getUserId();

		meetingService.deleteMeeting(meetingId, hostId);

		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "미팅 삭제 성공"));
	}

	@PutMapping("/{meetingId}")
	@ApiOperation(value = "미팅 수정", notes = "미팅 제목 수정")
	@ApiResponses({ @ApiResponse(code = 200, message = "미팅 제목 수정 성공"),
			@ApiResponse(code = 400, message = "존재하지 않는 미팅입니다. \n 호스트가 아닙니다. "),
			@ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<? extends BaseResponseBody> modify(@PathVariable("meetingId") int meetingId, String title,
			@AuthenticationPrincipal SsafyUserDetails ssafyUserDetails) {

		int hostId = ssafyUserDetails.getUser().getUserId();

		meetingService.updateMeeting(meetingId, title, hostId);

		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "미팅 제목 수정 성공"));
	}

	@PostMapping("/{meetingId}/join")
	@ApiOperation(value = "미팅 참가")
	@ApiResponses({ @ApiResponse(code = 200, message = "미팅 참가 신청 성공"),
			@ApiResponse(code = 400, message = "존재하지 않는 미팅입니다. \n 이미 최대 참가자가 참여한 미팅입니다. \n 존재하지 않는 유저입니다. \n 이미 참가한 유저입니다. \n 비밀번호가 일치하지 않습니다."),
			@ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<? extends BaseResponseBody> join(@PathVariable("meetingId") int meetingId, String password,
			@AuthenticationPrincipal SsafyUserDetails ssafyUserDetails) {

		int userId = ssafyUserDetails.getUser().getUserId();

		meetingService.joinMeeting(meetingId, password, userId);

		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "미팅 참가 신청 성공"));
	}
}
