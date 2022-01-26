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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Archive")
public class Archive {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "archiveId", columnDefinition = "INT UNSIGNED")
	private int archiveId;

	@Column(name = "archiveType", nullable = false)
	private ArchiveType archiveType;

	@Column(name = "archiveName", length = 45, nullable = false)
	private String archiveName;

	@Column(name = "path", length = 200, nullable = false)
	private String path;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userId", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "meetingId", nullable = false)
	private Meeting meeting;

	@Builder
	public Archive(ArchiveType archiveType, String archiveName, String path, User user, Meeting meeting) {
		this.archiveType = archiveType;
		this.archiveName = archiveName;
		this.path = path;
		this.user = user;
		this.meeting = meeting;
	}
}
