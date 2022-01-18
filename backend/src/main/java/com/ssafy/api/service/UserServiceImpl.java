package com.ssafy.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.db.entity.User;
import com.ssafy.db.repository.UserRepository;
import com.ssafy.db.repository.UserRepositorySupport;

/**
 *	유저 관련 비즈니스 로직 처리를 위한 서비스 구현 정의.
 */
@Service("userService")
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserRepositorySupport userRepositorySupport;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public String createUser(UserRegisterPostReq userRegisterInfo) {
		User user = new User();
		user.setEmail(userRegisterInfo.getEmail());
		user.setNickname(userRegisterInfo.getNickname());
		// 보안을 위해서 유저 패스워드 암호화 하여 디비에 저장.
		user.setPassword(passwordEncoder.encode(userRegisterInfo.getPassword()));
		if(getUserByEmail(user.getEmail()) == null) { // db에 유저 조회 -> 이메일 중복 검사
			if(getUserByNickname(user.getNickname()) == null) {
				userRepository.save(user);
				return "회원가입 성공";
			}
			else
				return "이미 등록된 닉네임입니다.";
		}
		else
			return "이미 등록된 이메일입니다.";
	}

	@Override
	public User getUserByEmail(String email) {
		// db에 유저 정보 조회 (email 를 통한 조회).
		User user = userRepositorySupport.findUserByEmail(email).get();
		return user;
	}
	
	@Override
	public User getUserByNickname(String nickname) {
		User user = userRepositorySupport.findUserByNickname(nickname).get();
		return user;
	}

	@Override
	public void deleteUser(int user_id) {
		userRepositorySupport.deleteByUserId(user_id);
	}
}
