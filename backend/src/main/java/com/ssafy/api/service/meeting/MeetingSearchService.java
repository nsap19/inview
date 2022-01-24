package com.ssafy.api.service.meeting;

import java.util.List;

import com.ssafy.api.request.meeting.MeetingRegisterPostReq;
import com.ssafy.api.response.CompanyRes;
import com.ssafy.api.response.IndustryRes;

public interface MeetingSearchService {
	void selectMeeting(MeetingRegisterPostReq registerInfo, int hostId);

	void detailMeeting(int meetingId);

	List<IndustryRes> selectIndustry();

	List<CompanyRes> selectCompany();

}
