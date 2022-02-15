package com.ssafy.db.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.api.response.LastMeetingDetailRes;
import com.ssafy.api.response.LastMeetingRes;
import com.ssafy.db.entity.Archive;
import com.ssafy.db.entity.QArchive;
import com.ssafy.db.entity.QCompany;
import com.ssafy.db.entity.QIndustry;
import com.ssafy.db.entity.QMeetingCompany;
import com.ssafy.db.entity.QParticipant;
import com.ssafy.db.entity.QUser;
import com.ssafy.db.entity.meeting.Meeting;
import com.ssafy.db.entity.meeting.QMeeting;

@Repository
public class LastMeetingRepositorySupport {

	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	QUser qUser = QUser.user;
	QParticipant qParticipant = QParticipant.participant;
	QArchive qArchive = QArchive.archive;
	QMeeting qMeeting = QMeeting.meeting;
	QIndustry qIndustry = QIndustry.industry;
	QMeetingCompany qMeetingCompany = QMeetingCompany.meetingCompany;
	QCompany qCompany = QCompany.company;

	public List<LastMeetingRes> findMeetingById(int userId, Pageable pageable) {

		List<Meeting> meetingList = jpaQueryFactory.select(qMeeting).from(qMeeting).leftJoin(qMeetingCompany)
				.on(qMeeting.eq(qMeetingCompany.meeting)).fetchJoin().leftJoin(qParticipant)
				.on(qParticipant.meeting.eq(qMeeting)).fetchJoin().leftJoin(qUser).on(qUser.eq(qParticipant.user))
				.fetchJoin().leftJoin(qCompany).on(qCompany.eq(qMeetingCompany.company)).fetchJoin().leftJoin(qIndustry)
				.on(qIndustry.eq(qMeeting.industry)).fetchJoin().groupBy(qMeeting).where(qUser.userId.eq(userId))
				.offset(pageable.getOffset()).limit(pageable.getPageSize()).orderBy(qMeeting.meetingId.desc()).fetch();

		List<LastMeetingRes> ret = meetingList.stream().map(m -> LastMeetingRes.builder().id(m.getMeetingId())
				.title(m.getTitle()).startTime(m.getStartTime()).endTime(m.getEndTime())
				.industryName(m.getIndustry().getIndustryName())
				.participantNicknameList(
						m.getParticipants().stream().map(p -> p.getUser().getNickname()).collect(Collectors.toList()))
				.companyNameList(m.getMeetingCompanies().stream().map(mc -> mc.getCompany().getCompanyName())
						.collect(Collectors.toList()))
				.build()).collect(Collectors.toList());

		return ret;
	}

	public LastMeetingDetailRes findMeetingDetailById(int userId, int meetingId) {
		Meeting meeting = jpaQueryFactory.select(qParticipant.meeting).from(qParticipant)
				.where(qParticipant.user.userId.eq(userId), qParticipant.meeting.meetingId.eq(meetingId)).fetchOne();

		List<Archive> archives = jpaQueryFactory.select(qArchive).from(qArchive)
				.where(qArchive.meeting.meetingId.eq(meetingId), qArchive.user.userId.eq(userId)).fetch();

		LastMeetingDetailRes meetingDetail = new LastMeetingDetailRes(meeting, archives);

		return meetingDetail;
	}
}
