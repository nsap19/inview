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
@ApiModel("TokenResponseBody")
public class TokenResponseBody extends BaseResponseBody {
	String token = null;
	
	public TokenResponseBody(Integer statusCode, String message, String token) {
		this.statusCode = statusCode;
		this.message = message;
		this.token = token;
	}
	
	public static TokenResponseBody of(Integer statusCode, String message, String token) {
		TokenResponseBody body = new TokenResponseBody();
		body.message = message;
		body.statusCode = statusCode;
		body.token = token;
		return body;
	}
}
