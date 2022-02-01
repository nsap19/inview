package com.ssafy.api.response;

import java.util.Date;
import java.util.List;

import com.ssafy.db.entity.Archive;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("LastMeetingDetailResponse")
public class LastMeetingDetailRes {
	@ApiModelProperty(name = "meeting_id", example = "1")
	int meetingId;
	@ApiModelProperty(name = "title", example = "취뽀합시다")
	String title;
	@ApiModelProperty(name = "startTime", example = "2022-01-28 11-22-33")
	Date startTime;
	@ApiModelProperty(name = "endTime", example = "2022-01-28 11-22-33")
	Date endTime;
	@ApiModelProperty(name = "password", example = "ssafy1234")
	String password;
	@ApiModelProperty(name = "userLimit", example = "6")
	int userLimit;
	@ApiModelProperty(name = "hostId", example = "6")
	int hostId;
	@ApiModelProperty(name = "industryId", example = "1")
	int industryId;
	@ApiModelProperty(name = "url", example = "a43dce2a-9666-499e-a4c9-de729b9f3694")
	String url;
	@ApiModelProperty(name = "closeTime", example = "2022-01-28 11-22-33")
	Date closeTime;
	@ApiModelProperty(name = "status", example = "false")
	boolean status;
	@ApiModelProperty(name = "archives_list", example = "아카이브")
	List<Archive> archives;
}
