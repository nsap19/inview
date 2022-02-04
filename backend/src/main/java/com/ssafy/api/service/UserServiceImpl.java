package com.ssafy.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.api.request.UserFindPwPostReq;
import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.common.exception.handler.NotExistsUserException;
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
	@Autowired
	EmailService emailService;
	
	@Override
	public void createUser(UserRegisterPostReq userRegisterInfo) {
		User user = new User();
		user.setEmail(userRegisterInfo.getEmail());
		user.setNickname(userRegisterInfo.getNickname());
		user.setPassword(passwordEncoder.encode(userRegisterInfo.getPassword())); // 패스워드 암호화하여 db에 저장
			
		try {
	    	emailService.sendSimpleMessage(userRegisterInfo.getEmail()); // 이메일 인증 코드 보내기
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
			
		userRepository.save(user);
	}
	
	public void findUser(UserFindPwPostReq userFindInfo) {
		
		String password = "";
		for (int i = 0; i < 12; i++) {
			password += (char) ((Math.random() * 26) + 97);
		}
		
		try {
	    	emailService.sendSimpleMessage(userFindInfo.getEmail()); // 이메일 인증 코드 보내기
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
		
		// 코드 확인 후 맞으면 임시 비밀번호 보내기 !!!!!!!!!!!!!!!!!!!!
		
		User user = getUserByEmail(userFindInfo.getEmail());
		user.setPassword(password);
		userRepository.save(user);
	}
	
	@Override
	public User getUserByUserId(int userId) {
		User user = userRepositorySupport.findUserByUserId(userId);
		return user;
	}
	@Override
	public User getUserByEmail(String email) {
		User user = userRepositorySupport.findUserByEmail(email);
		return user;
	}
	@Override
	public User getUserByNickname(String nickname) {
		User user = userRepositorySupport.findUserByNickname(nickname);
		return user;
	}

	@Override
	public void deleteUser(int userId) {
		User user = getUserByUserId(userId);
		userRepository.delete(user);
	}


	@Override
	public User getUserById(int userId) {
		return userRepository.findById(userId).orElseThrow(() -> new NotExistsUserException());
	}
}