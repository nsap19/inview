package com.ssafy.test;

import java.util.List;

import org.aspectj.lang.annotation.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ssafy.db.entity.Archive;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.meeting.Meeting;
import com.ssafy.db.repository.ArchiveRepository;
import com.ssafy.db.repository.ArchiveRepositorySupport;

public class BasicTest {

	@Autowired
	private ArchiveRepository archiveRepository;

	@Autowired
	private ArchiveRepositorySupport archiveRepositorySupport;

	@After(value = "")
	public void tearDown() throws Exception {
		archiveRepository.deleteById(1);
	}

	@Test
	public void querydsl_기본_기능_확인() {
		// given
		User user = new User();
		Meeting meeting = new Meeting();
		int archiveId = 1;
		int archiveType = 1;
		String archiveName = "test";
		String path = "C:\\WorkSpace\\web-project\\File";
		archiveRepository.save(new Archive(archiveId, archiveType, archiveName, path, user, meeting));

		// when
		List<Archive> result = archiveRepositorySupport.findAllByUserAndMeeting(user, meeting);

		// then
		System.out.println("size : " + result.size());
		System.out.println("path : " + result.get(0).getPath());
	}
}
