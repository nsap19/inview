package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("UserRegisterPostRequest")
public class UserRegisterPostReq {
	@ApiModelProperty(name="email", example="ssafy@ssafy.com")
	String email;
	@ApiModelProperty(name="nickname", example="ssafy")
	String nickname;
	@ApiModelProperty(name="password", example="your_password")
	String password;
}
