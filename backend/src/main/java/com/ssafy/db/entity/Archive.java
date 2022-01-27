package com.ssafy.db.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Archive {
	@Id
    @GeneratedValue
	private int archiveId;
	private int archiveType;
	private String archiveName;
	private String path;
	@ManyToOne
    @JoinColumn(name="user_id")
	private User user;
	// meeting id는 추후 추가...
}
