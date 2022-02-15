package com.ssafy.api.service;

import java.util.List;

import com.ssafy.api.request.UserUpdatePutReq;
import com.ssafy.api.response.LastMeetingDetailRes;
import com.ssafy.api.response.LastMeetingRes;

public interface MyPageService {
	void modifyUser(int userId, UserUpdatePutReq userUpdateInfo);

	List<LastMeetingRes> searchMeeting(int userId, int page);

	LastMeetingDetailRes searchMeetingDetail(int userid, int meetingId);
}