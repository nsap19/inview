package com.ssafy.api.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("UserLogoutGetRequest")
public class UserLogoutGetReq {
	@ApiModelProperty(name="HttpServletRequest 객체")
	HttpServletRequest requset;
	@ApiModelProperty(name="HttpServletResponse 객체")
	HttpServletResponse response;
}
