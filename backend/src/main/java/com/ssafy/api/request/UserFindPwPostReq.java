package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("UserFindPwPostRequest")
public class UserFindPwPostReq {
	@ApiModelProperty(name="email", example="ssafy@ssafy.com")
	String email;
}