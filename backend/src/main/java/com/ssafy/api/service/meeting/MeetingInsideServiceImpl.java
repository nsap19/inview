package com.ssafy.api.service.meeting;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
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

		if (meeting.getUser().getUserId() != hostId) {
			throw new NotHostException();
		}

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
	public void kickParticipant(int meetingId, int userId) {
		// DB에서 userId 삭제
		meetingRepository.findById(meetingId).orElseThrow(() -> new NotExistsMeetingException());

		Participant participant = participantRepository.findByMeetingIdAndUserId(meetingId, userId)
				.orElseThrow(() -> new NotExistsUserException());

		participantRepository.delete(participant);
	}

}
