package com.ssafy.db.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.db.entity.Participant;
import com.ssafy.db.entity.meeting.Meeting;

/**
 * 참가자 모델 관련 디비 쿼리 생성을 위한 JPA Query Method 인터페이스 정의.
 */
@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
	List<Participant> findByMeeting(Meeting meeting);

	@Query(value = "select p from Participant p where meetingId=:meetingId and userId=:userId")
	Optional<Participant> findByMeetingIdAndUserId(@Param("meetingId") int meetingId, @Param("userId") int userId);

	@Transactional
	@Modifying
	@Query(value = "delete from Participant where meetingId=:meetingId and userId=:userId", nativeQuery = true)
	void deleteByMeetingIdAndUserId(@Param("meetingId") int meetingId, @Param("userId") int userId);
}