package com.ssafy.db.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.ssafy.db.entity.meeting.Meeting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString(exclude = { "meeting" })
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MeetingCompany {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int meetingCompanyId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "meetingId")
	Meeting meeting;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "companyId")
	Company company;
}
