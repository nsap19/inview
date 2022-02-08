package com.ssafy.api.response;

import java.util.List;

import com.ssafy.db.entity.Archive;
import com.ssafy.db.entity.meeting.Meeting;

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
	@ApiModelProperty(name = "meeting")
	Meeting meeting;
	@ApiModelProperty(name = "archives_list", example = "아카이브")
	List<Archive> archives;
}
