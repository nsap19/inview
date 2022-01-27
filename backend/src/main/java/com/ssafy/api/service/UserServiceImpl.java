package com.ssafy.api.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.common.exception.handler.NotExistsMeetingException;
import com.ssafy.common.exception.handler.NotExistsUserException;
import com.ssafy.api.request.UserUpdatePutReq;
import com.ssafy.common.model.response.BaseResponseBody;
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
	public ResponseEntity<? extends BaseResponseBody> createUser(UserRegisterPostReq userRegisterInfo) {
		if(getUserByEmail(userRegisterInfo.getEmail()) != null) // 이메일 중복 검사
			return ResponseEntity.status(409).body(BaseResponseBody.of(409, "이미 등록된 이메일입니다."));
		else if(getUserByNickname(userRegisterInfo.getNickname()) == null) // 닉네임 중복 검사
			return ResponseEntity.status(409).body(BaseResponseBody.of(409, "이미 등록된 닉네임입니다."));
		
		String regx = "^[A-Za-z0-9+_.-]+@(.+)$";
		Pattern pattern = Pattern.compile(regx);
		Matcher matcher = pattern.matcher(userRegisterInfo.getEmail());
		if(!matcher.matches()) // 이메일 형식 유효성 검사
			return ResponseEntity.status(409).body(BaseResponseBody.of(409, "이메일 형식이 올바르지 않습니다."));
		if(userRegisterInfo.getPassword() == null || userRegisterInfo.getPassword().length() < 8) // 비밀번호 유효성 검사
			return ResponseEntity.status(409).body(BaseResponseBody.of(409, "비밀번호는 8글자 이상이어야 합니다."));
		
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
		return ResponseEntity.status(200).body(BaseResponseBody.of(409, "회원가입 성공"));
	}
	
	@Override
	public User getUserByUserId(int userId) {
		User user = userRepositorySupport.findUserByUserId(userId).get();
		return user;
	}
	@Override
	public User getUserByEmail(String email) {
		User user = userRepositorySupport.findUserByEmail(email).get();
		return user;
	}
	@Override
	public User getUserByNickname(String nickname) {
		User user = userRepositorySupport.findUserByNickname(nickname).get();
		return user;
	}
	
	@Override
	public ResponseEntity<? extends BaseResponseBody> modifyUser(int userId, UserUpdatePutReq userUpdateInfo) { // 회원정보 수정
		User user = getUserByUserId(userId);
		
		if(getUserByUserId(userId).getNickname() != userUpdateInfo.getNickname()) { // 닉네임을 변경하는 경우
			if(getUserByNickname(userUpdateInfo.getNickname()) == null) // 닉네임 중복 검사
				return ResponseEntity.status(409).body(BaseResponseBody.of(409, "이미 등록된 닉네임입니다."));
			else
				user.setNickname(userUpdateInfo.getNickname()); // 닉네임 변경
		}
		
		if(getUserByUserId(userId).getPassword() != userUpdateInfo.getPassword()) // 패스워드를 변경하는 경우
			user.setPassword(passwordEncoder.encode(userUpdateInfo.getPassword())); // 패스워드 암호화하여 db에 저장
		
		userRepository.save(user);
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "회원 정보 수정 성공"));
	}

	@Override
	public ResponseEntity<? extends BaseResponseBody> deleteUser(int userId) {
		User user = getUserByUserId(userId);
		userRepository.delete(user);
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "유저 탈퇴 성공"));
	}


	@Override
	public User getUserById(int userId) {
		return userRepository.findById(userId).orElseThrow(() -> new NotExistsUserException());
	}

}
