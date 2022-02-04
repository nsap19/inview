package com.ssafy.api.service;

import com.ssafy.db.entity.meeting.Meeting;

import java.util.List;

import com.ssafy.api.request.UserUpdatePutReq;
import com.ssafy.api.response.LastMeetingDetailRes;

public interface MyPageService {
	void modifyUser(int userId, UserUpdatePutReq userUpdateInfo);
	List<Meeting> searchMeeting(int userId);
	LastMeetingDetailRes searchMeetingDetail(int userid, int meetingId);
}