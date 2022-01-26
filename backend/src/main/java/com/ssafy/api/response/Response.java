package com.ssafy.api.response;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response {
	@ApiModelProperty
	private int statusCode;
	
	@ApiModelProperty
	private String message;
	
	@ApiModelProperty
	private Map<String, Object> data;
	
	public Response() {
		this(HttpStatus.OK);
	}
	
	public Response(HttpStatus httpStatus) {
		this.statusCode = httpStatus.value();
		this.message = httpStatus.getReasonPhrase();
		this.data = new HashMap<>();
	}
	
	public void add(String key, Object value) {
		this.data.put(key, value);
	}
}
