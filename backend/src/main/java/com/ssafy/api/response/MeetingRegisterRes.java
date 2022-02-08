package com.ssafy.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@ApiModel("MeetingRegisterRes")
public class MeetingRegisterRes {
	@ApiModelProperty(name = "아이디", example = "1")
	int id;
}