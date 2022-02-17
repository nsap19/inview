package com.ssafy.db.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.api.response.LastMeetingDetailRes;
import com.ssafy.api.response.MeetingRes;
import com.ssafy.db.entity.Archive;
import com.ssafy.db.entity.QArchive;
import com.ssafy.db.entity.QCompany;
import com.ssafy.db.entity.QIndustry;
import com.ssafy.db.entity.QMeetingCompany;
import com.ssafy.db.entity.QParticipant;
import com.ssafy.db.entity.QUser;
import com.ssafy.db.entity.meeting.Meeting;
import com.ssafy.db.entity.meeting.QMeeting;
import com.ssafy.db.entity.meeting.Status;

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

	public Page<MeetingRes> findFutureMeetingById(int userId, Pageable pageable) {
		List<Meeting> meetingList = jpaQueryFactory.select(qMeeting).from(qMeeting).leftJoin(qMeetingCompany)
				.on(qMeeting.eq(qMeetingCompany.meeting)).fetchJoin().leftJoin(qParticipant)
				.on(qParticipant.meeting.eq(qMeeting)).fetchJoin().leftJoin(qUser).on(qUser.eq(qParticipant.user))
				.fetchJoin().leftJoin(qCompany).on(qCompany.eq(qMeetingCompany.company)).fetchJoin().leftJoin(qIndustry)
				.on(qIndustry.eq(qMeeting.industry)).fetchJoin()
				.where(qUser.userId.eq(userId), qMeeting.status.eq(Status.WAITING), qMeeting.startTime.isNotNull())
				.groupBy(qMeeting).offset(pageable.getOffset()).limit(pageable.getPageSize())
				.orderBy(qMeeting.startTime.asc()).fetch();

		List<MeetingRes> ret = meetingList.stream().map(m -> MeetingRes.builder().id(m.getMeetingId())
				.title(m.getTitle()).startTime(m.getStartTime()).endTime(m.getEndTime()).userLimit(m.getUserLimit())
				.url(m.getUrl()).industryName(m.getIndustry().getIndustryName()).status(m.getStatus().toString())
				.participantNicknameList(
						m.getParticipants().stream().map(p -> p.getUser().getNickname()).collect(Collectors.toList()))
				.companyNameList(m.getMeetingCompanies().stream().map(mc -> mc.getCompany().getCompanyName())
						.collect(Collectors.toList()))
				.isLock(m.getPassword() == null ? false : true).build()).collect(Collectors.toList());

		// 페이징
		return new PageImpl<>(ret, pageable, ret.size());
	}

	public Page<MeetingRes> findMeetingById(int userId, Pageable pageable) {

		List<Meeting> meetingList = jpaQueryFactory.select(qMeeting).from(qMeeting).leftJoin(qMeetingCompany)
				.on(qMeeting.eq(qMeetingCompany.meeting)).fetchJoin().leftJoin(qParticipant)
				.on(qParticipant.meeting.eq(qMeeting)).fetchJoin().leftJoin(qUser).on(qUser.eq(qParticipant.user))
				.fetchJoin().leftJoin(qCompany).on(qCompany.eq(qMeetingCompany.company)).fetchJoin().leftJoin(qIndustry)
				.on(qIndustry.eq(qMeeting.industry)).fetchJoin().groupBy(qMeeting)
				.where(qUser.userId.eq(userId), qMeeting.status.eq(Status.CLOSING)).offset(pageable.getOffset())
				.limit(pageable.getPageSize()).orderBy(qMeeting.endTime.desc()).fetch();

		List<MeetingRes> ret = meetingList.stream().map(m -> MeetingRes.builder().id(m.getMeetingId())
				.title(m.getTitle()).startTime(m.getStartTime()).endTime(m.getEndTime()).userLimit(m.getUserLimit())
				.url(m.getUrl()).industryName(m.getIndustry().getIndustryName()).status(m.getStatus().toString())
				.participantNicknameList(
						m.getParticipants().stream().map(p -> p.getUser().getNickname()).collect(Collectors.toList()))
				.companyNameList(m.getMeetingCompanies().stream().map(mc -> mc.getCompany().getCompanyName())
						.collect(Collectors.toList()))
				.isLock(m.getPassword() == null ? false : true).build()).collect(Collectors.toList());

		return new PageImpl<>(ret, pageable, ret.size());
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
