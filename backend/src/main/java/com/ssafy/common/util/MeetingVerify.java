package com.ssafy.common.util;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ssafy.api.service.UserService;
import com.ssafy.api.service.meeting.MeetingInsideService;
import com.ssafy.db.entity.Participant;
import com.ssafy.db.entity.User;

@Component
public class MeetingVerify {
	@Autowired
	private static JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MeetingInsideService meetingInsideService;
	
	public User verifyToken(String token, int meetingId) {
		jwtTokenUtil.handleError(token);
		String email = Objects.requireNonNull(jwtTokenUtil.getUserEmailFromJwt(token)).trim();
		
		checkParticipant(meetingId, email);
		
		return userService.getUserByEmail(email);
	}
	
	public User verifyToken(String token, String meetingUrl) {
		jwtTokenUtil.handleError(token);
		String email = Objects.requireNonNull(jwtTokenUtil.getUserEmailFromJwt(token)).trim();
		int meetingId = meetingInsideService.getMeetingIdByUrl(meetingUrl);
		checkParticipant(meetingId, email);
		
		return userService.getUserByEmail(email);
	}
	
	public void checkForcedExitParticipant(int meetingId, String email) {
		User user = userService.getUserByEmail(email);
		meetingInsideService.checkForcedExit(meetingId, user.getUserId());
	}
	
	public void checkParticipant(int meetingId, String email) {
		User user = userService.getUserByEmail(email);
		Participant p = meetingInsideService.checkParticipant(meetingId, user.getUserId()); //해당 회의실 participant가 아닌경우 'NotExistsUserException' 발생
		
		System.out.println("participant : "+p.toString());
	}
}
