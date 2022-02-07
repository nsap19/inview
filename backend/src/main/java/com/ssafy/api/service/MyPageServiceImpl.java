package com.ssafy.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.api.request.UserUpdatePutReq;
import com.ssafy.api.response.LastMeetingDetailRes;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.meeting.Meeting;
import com.ssafy.db.repository.LastMeetingRepositorySupport;
import com.ssafy.db.repository.UserRepository;

@Service("myPageService")
public class MyPageServiceImpl implements MyPageService {

	@Autowired
	UserService userService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	LastMeetingRepositorySupport lastMeetingRepositorySupport;
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public void modifyUser(int userId, UserUpdatePutReq userUpdateInfo) { // 회원정보수정
		
		User user = userService.getUserByUserId(userId);
		user.setNickname(userUpdateInfo.getNickname()); // 닉네임 변경
		
		if (!userService.getUserByUserId(userId).getPassword().equals(passwordEncoder.encode(userUpdateInfo.getPassword()))) // 패스워드를 변경하는 경우
			user.setPassword(passwordEncoder.encode(userUpdateInfo.getPassword())); // 패스워드 암호화하여 db에 저장

		userRepository.save(user);
	}

	@Override
	public List<Meeting> searchMeeting(int userId) {
		return lastMeetingRepositorySupport.findMeetingById(userId);
	}

	@Override
	public LastMeetingDetailRes searchMeetingDetail(int userId, int meetingId) {
		return lastMeetingRepositorySupport.findMeetingDetailById(userId, meetingId);
	}
}