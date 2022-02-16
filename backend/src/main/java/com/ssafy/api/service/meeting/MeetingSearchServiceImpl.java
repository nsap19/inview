package com.ssafy.api.service.meeting;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.api.response.CompanyRes;
import com.ssafy.api.response.IndustryRes;
import com.ssafy.api.response.MeetingDetailRes;
import com.ssafy.api.response.MeetingRes;
import com.ssafy.common.exception.handler.NotExistsCompanyException;
import com.ssafy.common.exception.handler.NotExistsIndustryException;
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
	public Page<MeetingRes> selectMeeting(String title, List<String> industries, List<String> companies, int page) {
		PageRequest pageable = PageRequest.of(page - 1, 6);

		if (industries != null)
			industries.stream().forEach(i -> industryRepository.findByIndustryName(i)
					.orElseThrow(() -> new NotExistsIndustryException()).addCount());

		if (companies != null)
			companies.stream().forEach(c -> companyRepository.findByCompanyName(c)
					.orElseThrow(() -> new NotExistsCompanyException()).addCount());

		return meetingRepositorySupport.findByTitleOrIndustryOrCompany(title, industries, companies, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public MeetingDetailRes detailMeeting(int meetingId) {
		return meetingRepositorySupport.findById(meetingId);

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
