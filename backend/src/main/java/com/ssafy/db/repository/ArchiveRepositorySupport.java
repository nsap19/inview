package com.ssafy.db.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.db.entity.Archive;
import com.ssafy.db.entity.QArchive;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.meeting.Meeting;

/**
 * 아카이브 모델 관련 디비 쿼리 생성을 위한 구현 정의.
 */
@Repository
public class ArchiveRepositorySupport {
	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	QArchive qArchive = QArchive.archive;
	
	public List<Archive> findAll() {
		List<Archive> archiveList = jpaQueryFactory.selectFrom(qArchive).fetch();
		return archiveList;
	}

	public List<Archive> findAllByUserAndMeeting(User user, Meeting meeting) {
		List<Archive> archiveList = jpaQueryFactory.selectFrom(qArchive)
				.where(qArchive.meeting.meetingId.eq(meeting.getMeetingId()), qArchive.user.userId.eq(user.getUserId()))
				.fetch();

		return archiveList;
	}
	
	public Archive findByPathAndUser(String path, User user) {
		Archive archive = jpaQueryFactory.selectFrom(qArchive)
				.where(qArchive.user.userId.eq(user.getUserId()), qArchive.path.eq(path))
				.fetchOne();
		
		return archive;
	}
	
	public void deleteByArchiveId(int archiveId) {
		jpaQueryFactory.delete(qArchive)
			.where(qArchive.archiveId.eq(archiveId))
			.execute();
	}
}
