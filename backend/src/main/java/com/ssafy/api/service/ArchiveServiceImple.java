package com.ssafy.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.api.request.ArchiveRegisterPostReq;
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

	@Override
	public String createArchive(ArchiveRegisterPostReq archiveRegisterPostReq) {
		Archive archive = new Archive();
		archive.setArchiveName(archiveRegisterPostReq.getArchiveName());
		archive.setArchiveType(archiveRegisterPostReq.getArchiveType());
		archive.setPath(archiveRegisterPostReq.getPath());
		archive.setMeeting(archiveRegisterPostReq.getMeeting());
		archive.setUser(archiveRegisterPostReq.getUser());

		archiveRepository.save(archive);

		return "파일 생성 성공";
	}

	@Override
	public List<Archive> getArchives() {
		List<Archive> archives = archiveRepositorySupport.findAll();
		return archives;
	}

	@Override
	public List<Archive> getArchivesById(User user, Meeting meeting) {
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

}
