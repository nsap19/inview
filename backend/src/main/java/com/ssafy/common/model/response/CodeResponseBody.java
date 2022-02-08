package com.ssafy.common.model.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("CodeResponseBody")
public class CodeResponseBody extends BaseResponseBody {
	String code = null;
	
	public CodeResponseBody(Integer statusCode, String message, String code) {
		this.statusCode = statusCode;
		this.message = message;
		this.code = code;
	}
	
	public static CodeResponseBody of(Integer statusCode, String message, String code) {
		CodeResponseBody body = new CodeResponseBody();
		body.message = message;
		body.statusCode = statusCode;
		body.code = code;
		return body;
	}
}
