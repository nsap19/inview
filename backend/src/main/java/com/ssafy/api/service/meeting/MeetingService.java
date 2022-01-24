package com.ssafy.api.service.meeting;

import com.ssafy.api.request.meeting.MeetingRegisterPostReq;

/**
 * 미팅 관련 비즈니스 로직 처리를 위한 서비스 인터페이스 정의.
 */
public interface MeetingService {
	void createMeeting(MeetingRegisterPostReq registerInfo, int hostId);

	void deleteMeeting(int meetingId, int hostId);

	void updateMeeting(int meetingId, String title, int hostId);

	void joinMeeting(int meetingId, String password, int userId);
	
	void closeMeeting(int meetingId);
}