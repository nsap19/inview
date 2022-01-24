package com.ssafy.api.controller.meeting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.api.service.meeting.MeetingSearchService;
import com.ssafy.common.model.response.AdvancedResponseBody;
import com.ssafy.common.model.response.BaseResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "미팅 API", tags = { "Meeting" })
@RestController
@RequestMapping("/meeting")
public class MeetingSearchController {
	@Autowired
	MeetingSearchService meetingSearchService;

//	@GetMapping
//	@ApiOperation(value = "미팅 조회")
//	@ApiResponses({ @ApiResponse(code = 200, message = "미팅 전체 조회 성공"),
//			@ApiResponse(code = 400, message = "미팅 전체 조회 실패"), @ApiResponse(code = 500, message = "서버 오류") })
//	public ResponseEntity<? extends BaseResponseBody> search(@RequestParam(required = false) String title,
//			@RequestParam(required = false) String industry, @RequestParam(required = false) String company) {
//
//		// 임의로 리턴된 User 인스턴스. 현재 코드는 회원 가입 성공 여부만 판단하기 때문에 굳이 Insert 된 유저 정보를 응답하지 않음.
////		User user = userService.createUser(registerInfo);
//
//		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "미팅 전체 조회 성공"));
//	}
//
//	@GetMapping("/{meetingId}")
//	@ApiOperation(value = "미팅 상세 조회")
//	@ApiResponses({ @ApiResponse(code = 200, message = "{meetingId} 미팅실 상세 내역 조회 성공"),
//			@ApiResponse(code = 400, message = "존재하지 않는 미팅입니다."), @ApiResponse(code = 500, message = "서버 오류") })
//	public ResponseEntity<? extends BaseResponseBody> detail(@PathVariable("meetingId") int meetingId) {
//
//		meetingSearchService.detailMeeting(meetingId);
//
//		return ResponseEntity.status(200).body(BaseResponseBody.of(200, meetingId + " 미팅실 상세 내역 조회 성공"));
//	}

	@GetMapping("/industry")
	@ApiOperation(value = "전체 직군 조회")
	@ApiResponses({ @ApiResponse(code = 200, message = "전체 직군 조회 완료"), @ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<? extends BaseResponseBody> industry() {

		return ResponseEntity.status(200)
				.body(AdvancedResponseBody.of(200, "전체 직군 조회 완료", meetingSearchService.selectIndustry()));
	}

	@GetMapping("/company")
	@ApiOperation(value = "전체 회사명 조회")
	@ApiResponses({ @ApiResponse(code = 200, message = "전체 기업명 조회 완료"), @ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<? extends BaseResponseBody> company() {

		// 임의로 리턴된 User 인스턴스. 현재 코드는 회원 가입 성공 여부만 판단하기 때문에 굳이 Insert 된 유저 정보를 응답하지 않음.
//		User user = userService.createUser(registerInfo);

		return ResponseEntity.status(200)
				.body(AdvancedResponseBody.of(200, "전체 기업명 조회 완료", meetingSearchService.selectCompany()));
	}
}
