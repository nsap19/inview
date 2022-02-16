package com.ssafy.api.service.meeting;

import java.time.LocalDateTime;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.ssafy.common.exception.handler.NotExistsMeetingException;
import com.ssafy.common.exception.handler.NotExistsUserException;
import com.ssafy.common.exception.handler.NotHostException;
import com.ssafy.db.entity.Participant;
import com.ssafy.db.entity.meeting.Meeting;
import com.ssafy.db.entity.meeting.Status;
import com.ssafy.db.repository.MeetingRepository;
import com.ssafy.db.repository.ParticipantRepository;
import com.ssafy.db.repository.UserRepository;

@Service("meetingInsideService")
public class MeetingInsideServiceImpl implements MeetingInsideService {

	@Autowired
	MeetingRepository meetingRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ParticipantRepository participantRepository;

	@Transactional
	@Override
	public void closeMeeting(int meetingId, int hostId) {

		// 미팅 종료 시간 삽입 및 상태 변경
		Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new NotExistsMeetingException());

//		if (meeting.getUser().getUserId() != hostId) {
//			throw new NotHostException();
//		}

		meeting.setCloseTime(LocalDateTime.now());
		meeting.setStatus(Status.CLOSING);
	}

	@Override
	public Meeting getMeeting(int meetingId) {
		Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new NotExistsMeetingException());
		return meeting;
	}

	@Override
	@Transactional
	public void quitParticipant(int meetingId, int userId) {
		// DB에서 userId 삭제
		meetingRepository.findById(meetingId).orElseThrow(() -> new NotExistsMeetingException());

		Participant participant = this.checkParticipant(meetingId, userId);

		participantRepository.delete(participant);
	}

	@Override
	public Participant checkParticipant(int meetingId, int userId) {
		Participant participant = participantRepository.findByMeetingIdAndUserId(meetingId, userId)
				.orElseThrow(() -> new NotExistsUserException());
		return participant;
	}

	@Override
	@Transactional
	@Modifying
	public void forcedExit(int meetingId, int userId) {
		meetingRepository.findById(meetingId).orElseThrow(() -> new NotExistsMeetingException());

		Participant participant = this.checkParticipant(meetingId, userId);
		
		participant.setForcedExit(1);
		participantRepository.updateForcedExit(meetingId, userId);
	}

	@Override
	public int getMeetingIdByUrl(String meetingUrl) {
		Meeting meeting = meetingRepository.findByUrl(meetingUrl).orElseThrow(() -> new NotExistsMeetingException());
		
		return meeting.getMeetingId();
	}

	@Override
	public void checkForcedExit(int meetingId, int userId) {
		Long count = participantRepository.findCountByMeetingIdAndUserId(meetingId, userId);
		if(count!=0) new NotExistsUserException();
	}

}
