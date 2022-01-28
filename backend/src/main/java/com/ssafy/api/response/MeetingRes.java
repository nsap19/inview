package com.ssafy.api.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@ApiModel("MeetingResponse")
public class MeetingRes {
	@ApiModelProperty(name = "아이디", example = "1")
	int id;

	@ApiModelProperty(name = "방제목", example = "방제목")
	String title;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(name = "시작 시간", example = "2022-01-18 11:15:06")
	LocalDateTime startTime;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(name = "종료 예상 시간", example = "2022-01-18 11:15:06")
	LocalDateTime endTime;

	@ApiModelProperty(name = "참가자 닉네임 리스트", example = "[닉네임1, 닉네임2]")
	@Builder.Default
	List<String> participantNicknameList = new ArrayList<String>();

	@ApiModelProperty(name = "참여 인원 제한", example = "6")
	int userLimit;

	@ApiModelProperty(example = "f7217158-8ce5-4e01-913e-5bb349e46bdf")
	String url;

	@ApiModelProperty(name = "직무 이름", example = "IT")
	String industryName;

	@ApiModelProperty(name = "회사 이름", example = "[네이버]")
	List<String> companyNameList;

	@ApiModelProperty(name = "미팅 상태", example = "WAITING")
	String status;
}
