package com.ssafy.api.service;

import org.springframework.data.domain.Page;

import com.ssafy.api.request.UserUpdatePutReq;
import com.ssafy.api.response.LastMeetingDetailRes;
import com.ssafy.api.response.LastMeetingRes;

public interface MyPageService {
	void modifyUser(int userId, UserUpdatePutReq userUpdateInfo);

	Page<LastMeetingRes> searchMeeting(int userId, int page);

	LastMeetingDetailRes searchMeetingDetail(int userid, int meetingId);
}