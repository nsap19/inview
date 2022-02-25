package com.ssafy.api.controller.meeting;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.api.service.ArchiveService;
import com.ssafy.api.service.UserService;
import com.ssafy.api.service.meeting.MeetingInsideService;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.common.util.ArchiveUtil;
import com.ssafy.common.util.CurrentUser;
import com.ssafy.db.entity.ArchiveType;
import com.ssafy.db.entity.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "미팅 API", tags = { "Meeting" })
@RestController
@RequestMapping("/meeting")
public class MeetingInsideCotroller {
	@Autowired
	MeetingInsideService meetingInsideService;

	@Autowired
	ArchiveService arhciveService;

	@Autowired
	UserService userService;
	
	@Autowired
	ArchiveUtil archiveUtil;

	@PostMapping("/{meetingId}/start")
	@ApiOperation(value = "미팅 시작")
	@ApiResponses({ @ApiResponse(code = 200, message = "미팅 시작 성공"),
		@ApiResponse(code = 400, message = "존재하지 않는 미팅입니다. \n 이미 종료된 미팅입니다. \n 호스트가 아닙니다."),
		@ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<? extends BaseResponseBody> start(@PathVariable("meetingId") int meetingId) {
		meetingInsideService.startMeeting(meetingId, CurrentUser.getUserId());
		
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "미팅 시작 성공"));
	}
	
	@PostMapping("/{meetingId}/close")
	@ApiOperation(value = "미팅 종료")
	@ApiResponses({ @ApiResponse(code = 200, message = "미팅 종료 성공"),
			@ApiResponse(code = 400, message = "존재하지 않는 미팅입니다. \n 이미 종료된 미팅입니다. \n 호스트가 아닙니다."),
			@ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<? extends BaseResponseBody> close(@PathVariable("meetingId") int meetingId) {

		meetingInsideService.closeMeeting(meetingId, CurrentUser.getUserId());

		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "미팅 종료 성공"));
	}

	@PostMapping("/{meetingId}/upload")
	@ApiOperation(value = "미팅 중 파일 업로드")
	@ApiResponses({ @ApiResponse(code = 200, message = "파일 업로드 성공"), @ApiResponse(code = 400, message = "파일 업로드 실패") })
	public ResponseEntity<? extends BaseResponseBody> upload(@RequestParam("file") MultipartFile files,
			@PathVariable("meetingId") int meetingId, @RequestParam("archive-type") String archiveType, @RequestParam(required = false, value = "user-id")String userId) {
		try {
			// 현재 시간, 같은 파일명 업로드시 구분을 위한 salt로 사용
			ArchiveType saveArchiveType = ArchiveType.valueOf(archiveType.toUpperCase());
			String origFilename = files.getOriginalFilename();
			String filename = archiveUtil.getFilename(saveArchiveType, null, origFilename, null);
			/* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
			String savepath = archiveUtil.getSavepath(saveArchiveType, String.valueOf(meetingId));
			/* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
			if (!new File(savepath).exists()) {
				try {
					new File(savepath).mkdirs();
				} catch (Exception e) {
					e.getStackTrace();
				}
			}
			String filepath = archiveUtil.getFilepath(saveArchiveType, savepath, filename);
			files.transferTo(new File(filepath));
			User user = null;
			if(userId != null) {
				user = userService.getUserById(Integer.parseInt(userId));
			}
			archiveUtil.InsertToArchive(saveArchiveType, String.valueOf(meetingId), filepath, filename, user);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "파일 업로드 실패"));
		}
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "파일 업로드 성공"));
	}

	@DeleteMapping("/{meetingId}/users/{userId}")
	@ApiOperation(value = "참가자 퇴장")
	@ApiResponses({ @ApiResponse(code = 200, message = "참가자 퇴장 성공"),
			@ApiResponse(code = 400, message = "존재하지 않는 미팅입니다. \n 존재하지 않는 유저입니다."),
			@ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<? extends BaseResponseBody> quit(@PathVariable("meetingId") int meetingId,
			@PathVariable("userId") int userId) {

		meetingInsideService.quitParticipant(meetingId, userId);

		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "참가자 퇴장 성공"));
	}
	
	@DeleteMapping("/{meetingId}/users/{userId}/forcedExit")
	@ApiOperation(value = "참가자 강제 퇴장")
	@ApiResponses({ @ApiResponse(code = 200, message = "참가자 강제 퇴장 성공"),
			@ApiResponse(code = 400, message = "존재하지 않는 미팅입니다. \n 존재하지 않는 유저입니다."),
			@ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<? extends BaseResponseBody> forcedExit(@PathVariable("meetingId") int meetingId,
			@PathVariable("userId") int userId) {

		meetingInsideService.forcedExit(meetingId, userId);

		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "참가자 강제 퇴장 성공"));
	}
}
