package com.ssafy.common.exception.handler;

public class NotExistsIndustryException extends RuntimeException {
	public NotExistsIndustryException() {
		super("존재하지 않는 직군입니다.");
	}
}