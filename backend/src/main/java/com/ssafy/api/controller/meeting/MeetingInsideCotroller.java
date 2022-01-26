package com.ssafy.api.controller.meeting;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.api.request.ArchiveRegisterPostReq;
import com.ssafy.api.service.ArchiveService;
import com.ssafy.api.service.meeting.MeetingInsideService;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.common.util.MD5Generator;
import com.ssafy.db.entity.ArchiveType;
import com.ssafy.db.entity.meeting.Meeting;

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

	@PostMapping("/{meetingId}/close")
	@ApiOperation(value = "미팅 종료")
	@ApiResponses({ @ApiResponse(code = 200, message = "미팅 종료 성공"),
			@ApiResponse(code = 400, message = "존재하지 않는 미팅입니다. \n 이미 종료된 미팅입니다."),
			@ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<? extends BaseResponseBody> close(@PathVariable("meetingId") int meetingId) {

		meetingInsideService.closeMeeting(meetingId);

		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "미팅 종료 성공"));
	}

	@PostMapping("/{meetingId}/upload")
	@ApiOperation(value = "미팅 중 파일 업로드")
	@ApiResponses({ @ApiResponse(code = 200, message = "파일 업로드 성공"),
			@ApiResponse(code = 400, message = "파일 업로드 실패") })
	public ResponseEntity<? extends BaseResponseBody> upload(@RequestParam("file") MultipartFile files,
			@PathVariable("meetingId") int meetingId) {
		try {
			String origFilename = files.getOriginalFilename();
			String filename = new MD5Generator(origFilename).toString() + "_" + origFilename;
			/* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
			String savePath = System.getProperty("user.dir") + "\\files\\" + meetingId + "\\file";
			/* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
			if (!new File(savePath).exists()) {
				try {
					new File(savePath).mkdirs();
				} catch (Exception e) {
					e.getStackTrace();
				}
			}
			String filePath = savePath + "\\" + filename;
			System.out.println(filePath);
			files.transferTo(new File(filePath));

			ArchiveRegisterPostReq archiveRegisterPostReq = new ArchiveRegisterPostReq();
			archiveRegisterPostReq.setArchiveName(filename);
			archiveRegisterPostReq.setPath(filePath);
			archiveRegisterPostReq.setArchiveType(ArchiveType.FILE); // file

			// meeting 가져와서 설정하기
			Meeting meeting = meetingInsideService.getMeeting(meetingId);
			archiveRegisterPostReq.setMeeting(meeting);

			arhciveService.createAllArchive(archiveRegisterPostReq);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "파일 업로드 실패"));
		}
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "파일 업로드 성공"));
	}
}
