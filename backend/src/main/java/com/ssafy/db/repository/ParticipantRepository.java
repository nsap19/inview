package com.ssafy.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.db.entity.Participant;
import com.ssafy.db.entity.meeting.Meeting;

/**
 * 참가자 모델 관련 디비 쿼리 생성을 위한 JPA Query Method 인터페이스 정의.
 */
@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
	List<Participant> findByMeeting(Meeting meeting);
}