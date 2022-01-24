package com.ssafy.api.service.meeting;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.api.request.meeting.MeetingRegisterPostReq;
import com.ssafy.api.response.CompanyRes;
import com.ssafy.api.response.IndustryRes;
import com.ssafy.common.exception.handler.NotExistsMeetingException;
import com.ssafy.db.repository.CompanyRepository;
import com.ssafy.db.repository.IndustryRepository;
import com.ssafy.db.repository.MeetingRepository;
import com.ssafy.db.repository.MeetingRepositorySupport;

/**
 * 미팅 관련 비즈니스 로직 처리를 위한 서비스 구현 정의.
 */
@Service("meetingSearchService")
public class MeetingSearchServiceImpl implements MeetingSearchService {

	@Autowired
	MeetingRepository meetingRepository;

	@Autowired
	IndustryRepository industryRepository;

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	MeetingRepositorySupport meetingRepositorySupport;

	@Override
	@Transactional(readOnly = true)
	public void selectMeeting(MeetingRegisterPostReq registerInfo, int hostId) {
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional(readOnly = true)
	public void detailMeeting(int meetingId) {
		meetingRepository.findById(meetingId).orElseThrow(() -> new NotExistsMeetingException());

		meetingRepositorySupport.findById(meetingId);

	}

	@Override
	@Transactional(readOnly = true)
	public List<IndustryRes> selectIndustry() {
		return industryRepository.findAll().stream().map(i -> IndustryRes.of(i)).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<CompanyRes> selectCompany() {
		return companyRepository.findAll().stream().map(i -> CompanyRes.of(i)).collect(Collectors.toList());

	}

}
