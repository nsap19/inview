package com.ssafy.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ColumnDefault;
import com.ssafy.db.entity.meeting.Meeting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString(exclude = { "meeting" })
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Participant {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int participantId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "userId")
	User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "meetingId")
	Meeting meeting;

	@Column(nullable = false)
	@ColumnDefault("0") //1인 경우 강퇴당한 유저
	int forcedExit;
}
