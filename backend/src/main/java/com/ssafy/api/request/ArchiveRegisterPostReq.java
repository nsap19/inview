package com.ssafy.api.request;

import com.ssafy.db.entity.ArchiveType;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.meeting.Meeting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 데이터 파일화 ([POST] /-) 요청에 필요한 리퀘스트 바디 정의.
 */
@Getter
@Setter
@ApiModel("ArchiveRegisterPostRequest")
public class ArchiveRegisterPostReq {
	@ApiModelProperty(name = "파일  type", example = "1")
	ArchiveType archiveType;
	@ApiModelProperty(name = "파일 name", example = "Invie_Logo")
	String archiveName;
	@ApiModelProperty(name = "파일 path", example = "C:\\Invie\\Archive\\Chat\\archiveName")
	String path;
	@ApiModelProperty(name = "meetingId", example = "1")
	Meeting meeting;
	@ApiModelProperty(name = "userId", example = "1")
	User user;
}
