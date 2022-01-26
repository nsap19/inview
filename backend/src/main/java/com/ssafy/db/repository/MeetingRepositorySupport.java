package com.ssafy.db.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.api.response.MeetingDetailRes;
import com.ssafy.db.entity.QParticipant;
import com.ssafy.db.entity.meeting.QMeeting;

@Repository
public class MeetingRepositorySupport {
	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	QMeeting qMeeting = QMeeting.meeting;
	QParticipant qParticipant = QParticipant.participant;

	public MeetingDetailRes findById(int meetingId) {
		// 참가자 넘겨주기
		List<Tuple> ret = jpaQueryFactory
				.select(qMeeting.title, qMeeting.startTime, qMeeting.closeTime, qParticipant.user).from(qMeeting)
				.leftJoin(qParticipant).on(qMeeting.eq(qParticipant.meeting)).where(qMeeting.meetingId.eq(meetingId))
				.fetch();

		System.out.println(ret);
		return null;
	}

//	public Optional<User> findUserByEmail(String email) {
//		User user = jpaQueryFactory.select(qUser).from(qUser).where(qUser.email.eq(email)).fetchOne();
//		if (user == null)
//			return Optional.empty();
//		return Optional.ofNullable(user);
//	}
//
//	public Optional<User> findUserByNickname(String nickname) {
//		User user = jpaQueryFactory.select(qUser).from(qUser).where(qUser.nickname.eq(nickname)).fetchOne();
//		if (user == null)
//			return Optional.empty();
//		return Optional.ofNullable(user);
//	}
//
//	public void deleteByUserId(int user_id) {
//		jpaQueryFactory.delete(qUser).where(qUser.user_id.eq(user_id)).execute();
//	}
}
