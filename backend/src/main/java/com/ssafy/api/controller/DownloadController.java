package com.ssafy.api.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.api.response.ArchiveRes;
import com.ssafy.api.service.ArchiveService;
import com.ssafy.api.service.UserService;
import com.ssafy.api.service.meeting.MeetingService;
import com.ssafy.common.model.response.AdvancedResponseBody;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.common.util.MD5Generator;
import com.ssafy.db.entity.Archive;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.meeting.Meeting;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 다운로드 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
@Api(value = "다운로드  API", tags = { "download" })
@RestController
@RequestMapping("/download")
public class DownloadController {
	@Autowired
	ArchiveService archiveService;

	@Autowired
	MeetingService meetingService;

	@Autowired
	UserService userService;

	@GetMapping("/meeting/{meetingId}/user/{userId}")
	@ApiOperation(value = "파일 리스트 조회")
	@ApiResponses({ @ApiResponse(code = 200, message = "파일 리스트 조회 성공"),
			@ApiResponse(code = 400, message = "파일 리스트 조회 실패") })
	public ResponseEntity<? extends BaseResponseBody> archiveList(@PathVariable("meetingId") int meetingId,
			@PathVariable("userId") int userId) {
		List<Archive> archiveList;
		try {
			User user = userService.getUserById(userId);
			Meeting meeting = meetingService.getMeetingById(meetingId);
			archiveList = archiveService.getArchivesByUserAndMeeting(user, meeting);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(400).body(new BaseResponseBody(400, "파일 리스트 조회 실패"));
		}

		return ResponseEntity.status(200)
				.body(new AdvancedResponseBody<List<ArchiveRes>>(200, "파일 리스트 조회 성공", ArchiveRes.of(archiveList)));
	}

	@GetMapping("/meeting/{meetingId}/user/{userId}/{archiveId}")
	@ApiOperation(value = "파일 다운로드")
	@ApiResponses({ @ApiResponse(code = 200, message = "파일 다운로드 성공"), @ApiResponse(code = 400, message = "파일 다운로드 실패"),
			@ApiResponse(code = 500, message = "파일 손상, 다운로드 실패") })
	public ResponseEntity<Resource> archiveDownload(@PathVariable("meetingId") int meetingId,
			@PathVariable("archiveId") int archiveId, @PathVariable("userId") int userId,
			@RequestParam("archiveType") String archiveType) throws IOException {
		Archive archive;
		try {
			archive = archiveService.getArchivesById(archiveId);

			if (archive.getMeeting().getMeetingId() != meetingId || archive.getUser().getUserId() != userId
					|| !archive.getArchiveType().toString().equals(archiveType.toUpperCase())) {
				return ResponseEntity.status(400).body(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(400).body(null);
		}

		Path path = Paths.get(archive.getPath());
		String originalName = archive.getArchiveName();
		switch (archive.getArchiveType()) {
		case VIDEO:
			// video
			break;
		case MEMO:
			// memo
			break;

		case EVALUATION:
			// evaluation
			break;

		case FILE:
			// file
			StringTokenizer st = new StringTokenizer(archive.getArchiveName(), "_");
			String hash = st.nextToken();
			String salt = st.nextToken();
			StringBuilder sb = new StringBuilder();
			while (st.hasMoreTokens()) {
				sb.append(st.nextToken()).append("_");
			}
			sb.setLength(sb.length() - 1);
			originalName = String.valueOf(sb);
			try {
				String newHash = new MD5Generator(salt + originalName).toString();
				if (!newHash.equals(hash)) {
					return ResponseEntity.status(500).body(null);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(500).body(null);
			}
			break;
		case CHAT:
			// chat
			break;
		}
		Resource resource = new InputStreamResource(Files.newInputStream(path));
		return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream"))
				.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition")
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originalName + "\"")
				.body(resource);
	}
}
