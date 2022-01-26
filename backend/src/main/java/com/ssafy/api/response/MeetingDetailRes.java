package com.ssafy.api.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@ApiModel("MeetingDetailRes")
public class MeetingDetailRes {
	@ApiModelProperty(name = "아이디", example = "1")
	int id;

	@ApiModelProperty(name = "방제목", example = "방제목")
	String title;

	@ApiModelProperty(name = "미팅 시작 시간", example = "2022-01-18T11:15:06.000+00:00")
	Date startTime;

	@ApiModelProperty(name = "미팅 종료시간", example = "null")
	Date closeTime;

	@ApiModelProperty(name = "참여자 닉네임 리스트", example = "[닉네임1, 닉네임2]")
	@Builder.Default
	List<String> participantNicknameList = new ArrayList<String>();

}
