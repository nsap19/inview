package com.ssafy.db.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.api.response.LastMeetingDetailRes;
import com.ssafy.db.entity.Archive;
import com.ssafy.db.entity.QArchive;
import com.ssafy.db.entity.QParticipant;
import com.ssafy.db.entity.QUser;
import com.ssafy.db.entity.meeting.Meeting;
import com.ssafy.db.entity.meeting.QMeeting;


public class LastMeetingRepositorySupport {
	
	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	QUser qUser = QUser.user;
	QParticipant qParticipant = QParticipant.participant;
	QArchive qArchive = QArchive.archive;
	
	public List<Meeting> findMeetingById(int userId) {
		List<Meeting> lastMeetings = jpaQueryFactory
				.select(qParticipant.meeting)
				.from(qParticipant)
				.where(qParticipant.user.userId.eq(userId))
				.fetch();
		return lastMeetings;
	}
	
	public LastMeetingDetailRes findMeetingDetailById(int userId, int meetingId) {
		 Meeting meeting = jpaQueryFactory
				.select(qParticipant.meeting)
				.from(qParticipant)
				.where(qParticipant.user.userId.eq(userId), qParticipant.meeting.meetingId.eq(meetingId))
				.fetchOne();
		 
		 List<Archive> archives = jpaQueryFactory
				 .select(qArchive)
				 .from(qArchive)
				 .where(qArchive.meeting.meetingId.eq(meetingId), qArchive.user.userId.eq(userId))
				 .fetch();
		
		 LastMeetingDetailRes meetingDetail = new LastMeetingDetailRes(meeting, archives);
		 
		 return meetingDetail;
	}
}
