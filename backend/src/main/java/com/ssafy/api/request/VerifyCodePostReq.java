package com.ssafy.api.request;

import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("VerifyCodePostRequest")
public class VerifyCodePostReq {
	@ApiModelProperty(name="email 인증 코드", example="000000")
	Map<String, String> code;
}
