package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("UserUpdatePutRequest")
public class UserUpdatePutReq {
	@ApiModelProperty(name="유저 Email", example="ssafy@ssafy.com")
	String email;
	@ApiModelProperty(name="유저 Nickname", example="ssafy")
	String nickname;
	@ApiModelProperty(name="유저 Password", example="your_password")
	String password;
}
