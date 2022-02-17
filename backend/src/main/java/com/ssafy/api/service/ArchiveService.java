package com.ssafy.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.api.request.ArchiveRegisterPostReq;
import com.ssafy.db.entity.Archive;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.meeting.Meeting;

public interface ArchiveService {
	String createArchive(ArchiveRegisterPostReq archiveRegisterPostReq);
	
	String createAllArchive(ArchiveRegisterPostReq archiveRegisterPostReq);

	String createAllEvaluation(ArchiveRegisterPostReq archiveRegisterPostReq);

	List<Archive> getArchives();

	List<Archive> getArchivesByUserAndMeeting(User user, Meeting meeting);

	Archive getArchivesById(int archiveId);

	Archive findByPathAndUser(String path, User user);

	void deleteArchive(int archiveId);
}
