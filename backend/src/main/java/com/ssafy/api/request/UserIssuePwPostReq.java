package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("UserIssuePwPostRequest")
public class UserIssuePwPostReq {
	@ApiModelProperty(name="비밀번호 찾기 시 email", example="ssafy@ssafy.com")
	String email;
}
