package com.ssafy.api.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.api.service.ArchiveService;
import com.ssafy.db.entity.Archive;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.meeting.Meeting;

import io.swagger.annotations.Api;

/**
 * 다운로드 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
@Api(value = "다운로드  API", tags = { "download" })
@RestController
@RequestMapping("/download")
public class DownloadController {
	@Autowired
	ArchiveService archiveService;

	@GetMapping("/meeting/{meetingId}")
	public ResponseEntity<Resource> fileDownload(@PathVariable("archiveId") int archiveId) throws IOException {
		Meeting meeting = new Meeting();
		User user = new User();
		Archive archives = archiveService.getArchivesById(archiveId);
		Path path = Paths.get(archives.getPath());
		Resource resource = new InputStreamResource(Files.newInputStream(path));
		return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archives.getArchiveType() + "\"")
				.body(resource);
	}
}
