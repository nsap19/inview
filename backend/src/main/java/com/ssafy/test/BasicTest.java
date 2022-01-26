package com.ssafy.test;

import java.util.List;

import org.aspectj.lang.annotation.After;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ssafy.db.entity.Archive;
import com.ssafy.db.entity.ArchiveType;
import com.ssafy.db.entity.Industry;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.meeting.Meeting;
import com.ssafy.db.entity.meeting.Status;
import com.ssafy.db.repository.ArchiveRepository;
import com.ssafy.db.repository.ArchiveRepositorySupport;
import com.ssafy.db.repository.IndustryRepository;
import com.ssafy.db.repository.MeetingRepository;
import com.ssafy.db.repository.UserRepository;
import com.ssafy.db.repository.UserRepositorySupport;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles
@Slf4j
public class BasicTest {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserRepositorySupport userRepositorySupport;

	@Autowired
	private IndustryRepository industryRepository;
	
	@Autowired
	private MeetingRepository meetingRepository;
	
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
		user.setNickname("test");
		user.setEmail("ssafy@ssafy.com");
		user.setPassword("test");
		int userId = userRepository.save(user).getUserId();
		
		Industry industry = new Industry();
		industry.setIndustryName("naver");
		int industryId = industryRepository.save(industry).getIndustryId();
		
		Meeting meeting = new Meeting();
		meeting.setTitle("testMeeting");
		meeting.setIndustry(industry);
		meeting.setUser(user);
		meeting.setStatus(Status.RUNNING);
		int meetingId = meetingRepository.save(meeting).getMeetingId();
		
		ArchiveType archiveType = ArchiveType.CHAT;
		String archiveName = "test";
		String path = "C:\\WorkSpace\\web-project\\File";
		int id = archiveRepository.save(Archive.builder()
				.archiveType(archiveType)
				.archiveName(archiveName)
				.path(path)
				.user(user)
				.meeting(meeting)
				.build()).getArchiveId();

		// when
		List<Archive> result = archiveRepositorySupport.findAllByUserAndMeeting(user, meeting);

		// then
		System.out.println("size : " + result.size());
		System.out.println("path : " + result.get(0).getPath());
	}
}
