package com.ssafy.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ssafy.common.auth.SsafyUserDetails;

public class CurrentUser {
	public static int getUserId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SsafyUserDetails ssafyUserDetails = (SsafyUserDetails) auth.getDetails();
		return ssafyUserDetails.getUser().getUserId();
	}
}
