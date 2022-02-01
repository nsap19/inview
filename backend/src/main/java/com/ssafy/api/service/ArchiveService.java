package com.ssafy.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.api.request.ArchiveRegisterPostReq;
import com.ssafy.db.entity.Archive;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.meeting.Meeting;

public interface ArchiveService {
	String createAllArchive(ArchiveRegisterPostReq archiveRegisterPostReq);

	String createArchive(ArchiveRegisterPostReq archiveRegisterPostReq);

	List<Archive> getArchives();

	List<Archive> getArchivesByUserAndMeeting(User user, Meeting meeting);

	Archive getArchivesById(int archiveId);

	Archive findByPathAndUser(String path, User user);

	void deleteArchive(int archiveId);
}
