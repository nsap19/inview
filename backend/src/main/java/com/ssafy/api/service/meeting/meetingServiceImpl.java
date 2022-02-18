package com.ssafy.api.service.meeting;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.api.request.meeting.MeetingRegisterPostReq;
import com.ssafy.api.response.MeetingJoinRes;
import com.ssafy.api.response.MeetingRegisterRes;
import com.ssafy.common.exception.handler.AlreadyFullParticipantException;
import com.ssafy.common.exception.handler.AlreadyJoinMeetingException;
import com.ssafy.common.exception.handler.AlreadyRunningMeetingException;
import com.ssafy.common.exception.handler.NotEqualPasswordException;
import com.ssafy.common.exception.handler.NotExistsCompanyException;
import com.ssafy.common.exception.handler.NotExistsIndustryException;
import com.ssafy.common.exception.handler.NotExistsMeetingException;
import com.ssafy.common.exception.handler.NotExistsUserException;
import com.ssafy.common.exception.handler.NotHostException;
import com.ssafy.common.exception.handler.NotParticipantException;
import com.ssafy.db.entity.Company;
import com.ssafy.db.entity.Industry;
import com.ssafy.db.entity.MeetingCompany;
import com.ssafy.db.entity.Participant;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.meeting.Meeting;
import com.ssafy.db.entity.meeting.Status;
import com.ssafy.db.repository.CompanyRepository;
import com.ssafy.db.repository.IndustryRepository;
import com.ssafy.db.repository.MeetingCompanyRepository;
import com.ssafy.db.repository.MeetingRepository;
import com.ssafy.db.repository.ParticipantRepository;
import com.ssafy.db.repository.UserRepository;

/**
 * 미팅 관련 비즈니스 로직 처리를 위한 서비스 구현 정의.
 */
@Service("meetingService")
public class meetingServiceImpl implements MeetingService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	IndustryRepository industryRepository;

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	MeetingRepository meetingRepository;

	@Autowired
	ParticipantRepository participantRepository;

	@Autowired
	MeetingCompanyRepository meetingCompanyRepository;

	@Override
	@Transactional
	public MeetingRegisterRes createMeeting(MeetingRegisterPostReq registerInfo, int hostId) {
		// host 찾기
		User host = userRepository.findById(hostId).orElseThrow(() -> new NotExistsUserException());

		// industry 찾기
		Industry industry = industryRepository.findByIndustryName(registerInfo.getIndustryName())
				.orElseThrow(() -> new NotExistsIndustryException());

		// 회사 저장
		List<Company> companyList = registerInfo.getCompanyNameList().stream()
				.map(c -> companyRepository.findByCompanyName(c).orElseThrow(() -> new NotExistsCompanyException()))
				.collect(Collectors.toList());

		// 비밀번호가 빈문자열이면 제거
		if (registerInfo.getPassword().trim().length() == 0)
			registerInfo.setPassword(null);

		// meeting 저장
		Meeting meeting = meetingRepository.save(registerInfo.toMeeting(host, industry));

		// 방장 participant 테이블에 저장
		participantRepository.save(Participant.builder().meeting(meeting).user(host).build());

		// meetingCompany 저장
		companyList.stream().forEach(
				c -> meetingCompanyRepository.save(MeetingCompany.builder().company(c).meeting(meeting).build()));

		return MeetingRegisterRes.builder().id(meeting.getMeetingId()).url(meeting.getUrl()).build();

	}

	@Override
	@Transactional
	public void deleteMeeting(int meetingId, int hostId) {
		Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new NotExistsMeetingException());

		if (!Status.WAITING.equals(meeting.getStatus())) {
			throw new AlreadyRunningMeetingException();
		} else {
			meetingRepository.deleteById(meetingId);
		}
	}

	@Override
	@Transactional
	@Modifying
	public void updateMeeting(int meetingId, String title, int hostId) {
		Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new NotExistsMeetingException());

		if (meeting.getUser().getUserId() != hostId) {
			throw new NotHostException();
		} else {
			meeting.setTitle(title);
		}

	}

	@Override
	@Transactional
	public MeetingJoinRes joinMeeting(int meetingId, String password, int userId) {
		Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new NotExistsMeetingException());

		List<Participant> participantList = participantRepository.findByMeeting(meeting);

		if (participantList.size() >= meeting.getUserLimit())
			throw new AlreadyFullParticipantException();

		User user = userRepository.findById(userId).orElseThrow(() -> new NotExistsUserException());

		boolean isParticipant = participantRepository.findByMeeting(meeting).stream()
				.anyMatch(p -> p.getUser().getUserId() == user.getUserId());

		if (meeting.getUser().getUserId() == user.getUserId() || isParticipant)
			return MeetingJoinRes.builder().url(meeting.getUrl()).build();
		if (meeting.getPassword() != null && !meeting.getPassword().equals(password))
			throw new NotEqualPasswordException();
		Participant participant = participantRepository.findByMeetingIdAndUserId(meetingId, userId)
				.orElse(Participant.builder().meeting(meeting).user(user).build());
		if (participant.getForcedExit() == 1)
			throw new AlreadyJoinMeetingException();

		participantRepository.save(participant);
		return MeetingJoinRes.builder().url(meeting.getUrl()).build();
	}

	@Override
	public void cancelParticipation(int meetingId, int userId) {
		Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new NotExistsMeetingException());

		List<Participant> participantList = participantRepository.findByMeeting(meeting);

		User user = userRepository.findById(userId).orElseThrow(() -> new NotExistsUserException());

		boolean isParticipant = participantRepository.findByMeeting(meeting).stream()
				.anyMatch(p -> p.getUser().getUserId() == userId);

		if (!isParticipant)
			throw new NotParticipantException();
		else if (Status.RUNNING.equals(meeting.getStatus()))
			throw new AlreadyRunningMeetingException();
		else {
			participantRepository.deleteByMeetingIdAndUserId(meeting.getMeetingId(), user.getUserId());
			return;
		}
	}

	@Override
	public Meeting getMeetingById(int meetingId) {
		return meetingRepository.findById(meetingId).orElseThrow(() -> new NotExistsMeetingException());
	}
}
