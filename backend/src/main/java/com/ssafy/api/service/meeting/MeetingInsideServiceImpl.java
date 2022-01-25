package com.ssafy.api.service.meeting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.common.exception.handler.NotExistsMeetingException;
import com.ssafy.db.entity.meeting.Meeting;
import com.ssafy.db.repository.MeetingRepository;

@Service("meetingInsideService")
public class MeetingInsideServiceImpl implements MeetingInsideService {

	@Autowired
	MeetingRepository meetingRepository;

	@Override
	public void closeMeeting(int meetingId) {
		// TODO Auto-generated method stub
	}

	@Override
	public Meeting getMeeting(int meetingId) {
		Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new NotExistsMeetingException());
		return meeting;
	}

}
