package com.ssafy.db.entity.meeting;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.db.entity.Industry;
import com.ssafy.db.entity.MeetingCompany;
import com.ssafy.db.entity.Participant;
import com.ssafy.db.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 미팅 모델 정의.
 */
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Meeting {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int meetingId;

	@Column(length = 100, nullable = false)
	String title;

	LocalDateTime startTime;

	LocalDateTime endTime;

	@Column(length = 100)
	String password;

	@Max(6)
	@Column(nullable = true)
	@ColumnDefault("6")
	int userLimit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hostId", nullable = false)
	User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "industryId", nullable = false)
	Industry industry;

	String url;

	LocalDateTime closeTime;

	@Column(nullable = false)
	@ColumnDefault("0")
	Status status;

	@JsonIgnore
	@OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
	List<Participant> participants = new ArrayList<Participant>();

	@JsonIgnore
	@OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
	List<MeetingCompany> meetingCompanies = new ArrayList<MeetingCompany>();
}
