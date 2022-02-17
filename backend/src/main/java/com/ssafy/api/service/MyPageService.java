package com.ssafy.api.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ssafy.api.request.UserUpdatePutReq;
import com.ssafy.api.response.LastMeetingDetailRes;
import com.ssafy.api.response.LastMeetingRes;
import com.ssafy.api.response.MeetingRes;
import com.ssafy.db.entity.meeting.Meeting;

public interface MyPageService {
	void modifyUser(int userId, UserUpdatePutReq userUpdateInfo);
	
	List<Meeting> searchFutureMeeting(int userId);
	Page<MeetingRes> searchFutureMeeting(int userId, int page);
	Page<MeetingRes> searchMeeting(int userId, int page);
	LastMeetingDetailRes searchMeetingDetail(int userid, int meetingId);
}