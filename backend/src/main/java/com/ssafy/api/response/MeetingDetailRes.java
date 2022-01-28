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
@ApiModel("MeetingDetailRes")
public class MeetingDetailRes {
	@ApiModelProperty(name = "아이디", example = "1")
	int id;

	@ApiModelProperty(name = "방제목", example = "방제목")
	String title;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(name = "미팅 시작 시간", example = "2022-01-18 11:15:06")
	LocalDateTime startTime;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(name = "미팅 종료시간", example = "null")
	LocalDateTime closeTime;

	@ApiModelProperty(name = "참여자 닉네임 리스트", example = "[닉네임1, 닉네임2]")
	@Builder.Default
	List<String> participantNicknameList = new ArrayList<String>();

}
