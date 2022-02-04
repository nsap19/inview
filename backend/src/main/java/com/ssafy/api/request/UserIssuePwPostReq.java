package com.ssafy.api.request;

import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("UserIssuePwPostRequest")
public class UserIssuePwPostReq {
	@ApiModelProperty(name="email 인증 코드", example="000000")
	Map<String, String> code;
	@ApiModelProperty(name="비밀번호 찾기 시 email", example="ssafy@ssafy.com")
	String email;
}
