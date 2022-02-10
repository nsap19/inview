package com.ssafy.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.api.request.ArchiveRegisterPostReq;
import com.ssafy.common.util.MeetingParticipant;
import com.ssafy.db.entity.Archive;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.meeting.Meeting;
import com.ssafy.db.repository.ArchiveRepository;
import com.ssafy.db.repository.ArchiveRepositorySupport;

@Service("archiveService")
public class ArchiveServiceImple implements ArchiveService {
	@Autowired
	ArchiveRepository archiveRepository;

	@Autowired
	ArchiveRepositorySupport archiveRepositorySupport;

	@Autowired
	private MeetingParticipant meetingParticipant;

	@Override
	public String createArchive(ArchiveRegisterPostReq archiveRegisterPostReq) {
		String path = archiveRegisterPostReq.getPath();
		User user = archiveRegisterPostReq.getUser();
		if (this.findByPathAndUser(path, user) == null) {
			Archive archive = new Archive();
			archive.setArchiveName(archiveRegisterPostReq.getArchiveName());
			archive.setArchiveType(archiveRegisterPostReq.getArchiveType());
			archive.setMeeting(archiveRegisterPostReq.getMeeting());
			archive.setUser(user);
			archive.setPath(path);
			archiveRepository.save(archive);
		}

		return "파일 생성 성공";
	}

	@Override
	public String createAllArchive(ArchiveRegisterPostReq archiveRegisterPostReq) {
		String path = archiveRegisterPostReq.getPath();
		String meetingId = String.valueOf(archiveRegisterPostReq.getMeeting().getMeetingId());
		System.out.println("meetingParticipant : " + meetingParticipant);
		List<User> participantList = meetingParticipant.getParticipantByMeetingId(meetingId);
		for (User user : participantList) {
			System.out.println(user.toString());
			if (this.findByPathAndUser(path, user) == null) {
				Archive archive = new Archive();
				archive.setArchiveName(archiveRegisterPostReq.getArchiveName());
				archive.setArchiveType(archiveRegisterPostReq.getArchiveType());
				archive.setMeeting(archiveRegisterPostReq.getMeeting());
				archive.setUser(user);
				archive.setPath(path);
				archiveRepository.save(archive);
			}
		}
		return "파일 생성 성공";
	}

	@Override
	public List<Archive> getArchives() {
		List<Archive> archives = archiveRepositorySupport.findAll();
		return archives;
	}

	@Override
	public List<Archive> getArchivesByUserAndMeeting(User user, Meeting meeting) {
		List<Archive> archives = archiveRepositorySupport.findAllByUserAndMeeting(user, meeting);
		return archives;
	}

	@Override
	public void deleteArchive(int archiveId) {
		archiveRepository.deleteByArchiveId(archiveId);
	}

	@Override
	public Archive getArchivesById(int archiveId) {
		return archiveRepository.findByArchiveId(archiveId);
	}

	@Override
	public Archive findByPathAndUser(String path, User user) {
		return archiveRepository.findByPathAndUser(path, user);
	}

}
