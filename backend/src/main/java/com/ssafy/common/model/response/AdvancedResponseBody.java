package com.ssafy.common.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdvancedResponseBody<T> extends BaseResponseBody {
	T data;

	public static <T> AdvancedResponseBody of(Integer statusCode, String message, T data) {
		BaseResponseBody body = new BaseResponseBody();
		body.message = message;
		body.statusCode = statusCode;
		return AdvancedResponseBody.builder().message(message).statusCode(statusCode).data(data).build();
	}

	@Builder
	public AdvancedResponseBody(Integer statusCode, String message, T data) {
		super(statusCode, message);
		this.data = data;
	}
}
