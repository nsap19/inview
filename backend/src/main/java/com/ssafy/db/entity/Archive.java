package com.ssafy.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ssafy.db.entity.meeting.Meeting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Arcive")
public class Archive {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "archive_id")
	private int archiveId;

	@Column(name = "archiveType", nullable = false)
	private int archiveType;

	@Column(name = "archiveName", length = 45, nullable = false)
	private String archiveName;

	@Column(name = "path", length = 200, nullable = false)
	private String path;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "meeting_id", nullable = false)
	private Meeting meeting;
}
