package com.ssafy.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@ApiModel("MeetingJoinRes")
public class MeetingJoinRes {
	@ApiModelProperty(name = "url", example = "809974be-a0ee-414a-a3db-acc1aadf4188")
	String url;
}