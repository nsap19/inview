package com.ssafy.db.repository;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.api.response.MeetingDetailRes;
import com.ssafy.api.response.MeetingRes;
import com.ssafy.common.exception.handler.NotExistsMeetingException;
import com.ssafy.db.entity.QCompany;
import com.ssafy.db.entity.QIndustry;
import com.ssafy.db.entity.QMeetingCompany;
import com.ssafy.db.entity.QParticipant;
import com.ssafy.db.entity.QUser;
import com.ssafy.db.entity.meeting.Meeting;
import com.ssafy.db.entity.meeting.QMeeting;
import com.ssafy.db.entity.meeting.Status;

@Repository
public class MeetingRepositorySupport {
	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	QMeeting qMeeting = QMeeting.meeting;
	QUser qUser = QUser.user;
	QParticipant qParticipant = QParticipant.participant;
	QIndustry qIndustry = QIndustry.industry;
	QMeetingCompany qMeetingCompany = QMeetingCompany.meetingCompany;
	QCompany qCompany = QCompany.company;

	public MeetingDetailRes findById(int meetingId) {
		List<Tuple> tuple = jpaQueryFactory
				.select(qMeeting.meetingId, qMeeting.title, qMeeting.startTime, qMeeting.closeTime, qUser.nickname,
						qMeeting.user.userId)
				.from(qMeeting).leftJoin(qParticipant).on(qMeeting.eq(qParticipant.meeting)).leftJoin(qUser)
				.on(qParticipant.user.eq(qUser)).where(qMeeting.meetingId.eq(meetingId)).fetch();

		if (tuple.size() == 0) {
			throw new NotExistsMeetingException();
		}

		MeetingDetailRes ret = MeetingDetailRes.builder().id(tuple.get(0).get(qMeeting.meetingId))
				.title(tuple.get(0).get(qMeeting.title)).startTime(tuple.get(0).get(qMeeting.startTime))
				.closeTime(tuple.get(0).get(qMeeting.closeTime)).hostId(tuple.get(0).get(qMeeting.user.userId)).build();

		tuple.stream().forEach(n -> ret.getParticipantNicknameList().add(n.get(qUser.nickname)));

		return ret;

	}

	public Page<MeetingRes> findByTitleOrIndustryOrCompany(String title, List<String> industries,
			List<String> companies, Pageable pageable) {

		BooleanBuilder builder = new BooleanBuilder();

		if (title != null) {
			builder.and(qMeeting.title.contains(title));
		}

		if (industries != null) {
			builder.and(qIndustry.industryName.in(industries));
		}

		if (companies != null) {
			builder.and(qCompany.companyName.in(companies));
		}
		
		builder.and(Expressions.dateTemplate(Date.class,"{0}",qMeeting.startTime)
				.after(
					Expressions.dateTemplate(Date.class,"{0}",Expressions.currentTimestamp())));
		
		builder.or(qMeeting.startTime.isNull());
		
		builder.andNot(qMeeting.status.eq(Status.CLOSING));
		
		List<Meeting> meetingList = jpaQueryFactory.select(qMeeting).from(qMeeting).leftJoin(qMeetingCompany)
				.on(qMeeting.eq(qMeetingCompany.meeting)).fetchJoin().leftJoin(qParticipant)
				.on(qParticipant.meeting.eq(qMeeting)).fetchJoin().leftJoin(qUser).on(qUser.eq(qParticipant.user))
				.fetchJoin().leftJoin(qCompany).on(qCompany.eq(qMeetingCompany.company)).fetchJoin().leftJoin(qIndustry)
				.on(qIndustry.eq(qMeeting.industry)).fetchJoin().where(builder).groupBy(qMeeting)
				.offset(pageable.getOffset()).limit(pageable.getPageSize()).orderBy(qMeeting.meetingId.desc()).fetch();

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
	
}
