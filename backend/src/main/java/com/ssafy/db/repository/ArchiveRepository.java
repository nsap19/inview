package com.ssafy.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.db.entity.Archive;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.meeting.Meeting;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Integer> {
	@Transactional
	List<Archive> findAll();

	@Transactional
	List<Archive> findAllByUserAndMeeting(User user, Meeting meeting);

	@Transactional
	Archive findByArchiveId(int archiveId);

	@Transactional
	Archive findByPathAndUser(String path, User user);

	@Transactional
	void deleteByArchiveId(int archiveId);
}
