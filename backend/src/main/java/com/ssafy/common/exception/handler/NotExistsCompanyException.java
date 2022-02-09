package com.ssafy.common.exception.handler;

public class NotExistsCompanyException extends RuntimeException {
	public NotExistsCompanyException() {
		super("존재하지 않는 기업명입니다.");
	}
}