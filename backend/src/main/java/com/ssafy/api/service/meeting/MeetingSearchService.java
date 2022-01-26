package com.ssafy.api.service.meeting;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ssafy.api.response.CompanyRes;
import com.ssafy.api.response.IndustryRes;
import com.ssafy.api.response.MeetingDetailRes;
import com.ssafy.api.response.MeetingRes;

public interface MeetingSearchService {
	Page<MeetingRes> selectMeeting(String title, List<String> industry, List<String> company, int page);

	MeetingDetailRes detailMeeting(int meetingId);

	List<IndustryRes> selectIndustry();

	List<CompanyRes> selectCompany();

}
