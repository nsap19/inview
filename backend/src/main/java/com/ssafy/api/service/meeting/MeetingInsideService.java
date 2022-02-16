package com.ssafy.api.service.meeting;

import com.ssafy.db.entity.Participant;
import com.ssafy.db.entity.meeting.Meeting;

/**
 * 미팅 내부 관련 비즈니스 로직 처리를 위한 서비스 인터페이스 정의.
 */
public interface MeetingInsideService {
	void startMeeting(int meetingId, int hostId);
	
	void closeMeeting(int meetingId, int hostId);

	Meeting getMeeting(int meetingId);

	void quitParticipant(int meetingId, int userId);
	
	void forcedExit(int meetingId, int userId);
	
	Participant checkParticipant(int meetingId, int userId);
	
	void checkForcedExit(int meetingId, int userId);
	
	int getMeetingIdByUrl(String meetingUrl);
}
