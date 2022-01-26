package com.ssafy.api.controller.meeting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.api.response.CompanyRes;
import com.ssafy.api.response.IndustryRes;
import com.ssafy.api.response.MeetingDetailRes;
import com.ssafy.api.response.MeetingRes;
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

	@GetMapping
	@ApiOperation(value = "미팅 조회", notes = "title and industryList and companyList 연산 실행. \n industryList와 companyList 내부에서는 or 연산 수행")
	@ApiResponses({
			@ApiResponse(code = 200, message = "미팅 전체 조회 성공", response = MeetingRes.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "존재하지 않는 직군입니다. \n 존재하지 않는 기업명입니다."),
			@ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<? extends BaseResponseBody> search(@RequestParam(required = false) String title,
			@RequestParam(required = false) List<String> industryList,
			@RequestParam(required = false) List<String> companyList,
			@RequestParam(value = "page", defaultValue = "1") int page) {

		return ResponseEntity.status(200).body(AdvancedResponseBody.of(200, "미팅 전체 조회 성공",
				meetingSearchService.selectMeeting(title, industryList, companyList, page)));
	}

	@GetMapping("/{meetingId}")
	@ApiOperation(value = "미팅 상세 조회")
	@ApiResponses({
			@ApiResponse(code = 200, message = "{meetingId} 미팅실 상세 내역 조회 성공", response = MeetingDetailRes.class),
			@ApiResponse(code = 400, message = "존재하지 않는 미팅입니다."), @ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<? extends BaseResponseBody> detail(@PathVariable("meetingId") int meetingId) {

		return ResponseEntity.status(200).body(AdvancedResponseBody.of(200, meetingId + " 미팅실 상세 내역 조회 성공",
				meetingSearchService.detailMeeting(meetingId)));
	}

	@GetMapping("/industry")
	@ApiOperation(value = "전체 직군 조회")
	@ApiResponses({
			@ApiResponse(code = 200, message = "전체 직군 조회 완료", response = IndustryRes.class, responseContainer = "List"),
			@ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<? extends BaseResponseBody> industry() {

		return ResponseEntity.status(200)
				.body(AdvancedResponseBody.of(200, "전체 직군 조회 완료", meetingSearchService.selectIndustry()));
	}

	@GetMapping("/company")
	@ApiOperation(value = "전체 회사명 조회")
	@ApiResponses({
			@ApiResponse(code = 200, message = "전체 기업명 조회 완료", response = CompanyRes.class, responseContainer = "List"),
			@ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<? extends BaseResponseBody> company() {

		return ResponseEntity.status(200)
				.body(AdvancedResponseBody.of(200, "전체 기업명 조회 완료", meetingSearchService.selectCompany()));
	}
}
