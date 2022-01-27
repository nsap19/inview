package com.ssafy.common.model.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("AdvancedResponseBody")
public class AdvancedResponseBody<T> {
	String message = null;
	Integer statusCode = null;
	T data;

	public static <T> AdvancedResponseBody of(Integer statusCode, String message, T data) {
		BaseResponseBody body = new BaseResponseBody();
		body.message = message;
		body.statusCode = statusCode;
		return AdvancedResponseBody.builder().message(message).statusCode(statusCode).data(data).build();
	}

	@Builder
	public AdvancedResponseBody(Integer statusCode, String message, T data) {
		this.statusCode = statusCode;
		this.message = message;
		this.data = data;
	}
}
