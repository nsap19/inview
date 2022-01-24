package com.ssafy.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.db.entity.Archive;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.meeting.Meeting;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Integer> {
	List<Archive> findAll();

	List<Archive> findAllByUserAndMeeting(User user, Meeting meeting);
	
	Archive findByArchiveId(int archiveId);
	
	void deleteByArchiveId(int archiveId);
}
