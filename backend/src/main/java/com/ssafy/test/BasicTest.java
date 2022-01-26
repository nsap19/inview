package com.ssafy.test;

import java.util.List;

import org.junit.After;
import org.junit.Before;
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
// 테스트를 사전순으로 실행시켜주는 어노테이션
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

	private User user1, user2;
	private Industry industry;
	private Meeting meeting;
	private Archive archive1, archive2;

	@After
	public void tearDown() throws Exception {
		
	}
	
//	@Before
	public void given() {
		// given
		user1 = new User();
		user1.setNickname("test");
		user1.setEmail("ssafy@ssafy.com");
		user1.setPassword("test");
		int user1Id = userRepository.save(user1).getUserId();

		user2 = new User();
		user2.setNickname("ssafy");
		user2.setEmail("test@ssafy.com");
		user2.setPassword("ssafy");
		int user2Id = userRepository.save(user2).getUserId();

		Industry industry = new Industry();
		industry.setIndustryName("naver");
		int industryId = industryRepository.save(industry).getIndustryId();

		meeting = new Meeting();
		meeting.setTitle("testMeeting");
		meeting.setIndustry(industry);
		meeting.setUser(user1);
		meeting.setStatus(Status.RUNNING);
		int meetingId = meetingRepository.save(meeting).getMeetingId();

		ArchiveType archiveType1 = ArchiveType.CHAT;
		ArchiveType archiveType2 = ArchiveType.FILE;
		String archiveName = "test";
		String path = "C:\\WorkSpace\\web-project\\";
		int id1 = archiveRepository.save(Archive.builder().archiveType(archiveType1).archiveName(archiveName)
				.path(path + archiveType1).user(user1).meeting(meeting).build()).getArchiveId();
		int id2 = archiveRepository.save(Archive.builder().archiveType(archiveType1).archiveName(archiveName)
				.path(path + archiveType1).user(user2).meeting(meeting).build()).getArchiveId();

		int id3 = archiveRepository.save(Archive.builder().archiveType(archiveType2).archiveName(archiveName)
				.path(path + archiveType2).user(user1).meeting(meeting).build()).getArchiveId();
	}

//	@Test
	public void querydsl2_find() {
		// when
		List<Archive> result = archiveRepository.findAllByUserAndMeeting(user1, meeting);
		int archiveId = result.get(0).getArchiveId();
		String resultPath = result.get(0).getPath();
		Archive archive1 = archiveRepository.findByArchiveId(archiveId);
		Archive archive2 = archiveRepository.findByPathAndUser(resultPath, user1);

		// then
		System.out.println("result size : " + result.size());
		System.out.println("path : " + result.get(0).getPath());
		System.out.println(archive1);
		System.out.println(archive2);
		
	}
	
	@Test
	public void querydsl3_delete() {
		// given
		user1 = userRepository.findById(1).get();
		meeting = meetingRepository.findById(1).get();
		
		// when
		List<Archive> result = archiveRepository.findAllByUserAndMeeting(user1, meeting);
		int archiveId = result.get(0).getArchiveId();
		String resultPath = result.get(0).getPath();
		Archive archive1 = archiveRepository.findByArchiveId(archiveId);
		Archive archive2 = archiveRepository.findByPathAndUser(resultPath, user1);

		// then
		System.out.println("result size : " + result.size());
		System.out.println("path : " + result.get(0).getPath());
		System.out.println(archive1);
		System.out.println(archive2);
		
		archiveRepository.deleteByArchiveId(1);
	}
}
