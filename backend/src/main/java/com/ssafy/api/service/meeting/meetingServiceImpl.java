package com.ssafy.api.service.meeting;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.api.request.meeting.MeetingRegisterPostReq;
import com.ssafy.common.exception.handler.AlreadyFullParticipantException;
import com.ssafy.common.exception.handler.AlreadyJoinMeetingException;
import com.ssafy.common.exception.handler.AlreadyRunningMeetingException;
import com.ssafy.common.exception.handler.NotEqualPasswordException;
import com.ssafy.common.exception.handler.NotExistsCompanyException;
import com.ssafy.common.exception.handler.NotExistsIndustryException;
import com.ssafy.common.exception.handler.NotExistsMeetingException;
import com.ssafy.common.exception.handler.NotExistsUserException;
import com.ssafy.common.exception.handler.NotHostException;
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
	public void createMeeting(MeetingRegisterPostReq registerInfo, int hostId) {
		// host 찾기
		User host = userRepository.findById(hostId).orElseThrow(() -> new NotExistsUserException());

		// industry 찾기
		Industry industry = industryRepository.findByIndustryName(registerInfo.getIndustry_name())
				.orElseThrow(() -> new NotExistsIndustryException());

		// 회사 찾기
		List<Company> companyList = registerInfo.getCompany_name_list().stream()
				.map(c -> companyRepository.findByCompanyName(c).orElseThrow(() -> new NotExistsCompanyException()))
				.collect(Collectors.toList());

		// meeting 저장
		Meeting meeting = meetingRepository.save(registerInfo.toMeeting(host, industry));

		// host 참가 테이블에 저장
		participantRepository.save(Participant.builder().meeting(meeting).user(host).build());

		// meetingCompany 저장
		companyList.stream().forEach(
				c -> meetingCompanyRepository.save(MeetingCompany.builder().company(c).meeting(meeting).build()));

	}

	@Override
	@Transactional
	public void deleteMeeting(int meetingId, int hostId) {
		Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new NotExistsMeetingException());

		if (meeting.getUser().getUserId() != hostId) {
			throw new NotHostException();
		} else if (!Status.WAITING.equals(meeting.getStatus())) {
			throw new AlreadyRunningMeetingException();
		} else {
			meetingRepository.deleteById(meetingId);
		}
	}

	@Override
	@Transactional
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
	public void joinMeeting(int meetingId, String password, int userId) {
		Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new NotExistsMeetingException());

		List<Participant> participantList = participantRepository.findByMeeting(meeting);

		if (participantList.size() >= meeting.getUserLimit())
			throw new AlreadyFullParticipantException();

		User user = userRepository.findById(userId).orElseThrow(() -> new NotExistsUserException());

		Boolean isAlreadyParticipant = participantRepository.findByMeeting(meeting).stream()
				.anyMatch(p -> p.equals(user));

		if (isAlreadyParticipant)
			throw new AlreadyJoinMeetingException();
		else if (meeting.getPassword() != null && meeting.getPassword().equals(password))
			throw new NotEqualPasswordException();
		else
			participantRepository.save(Participant.builder().meeting(meeting).user(user).build());

	}

}
