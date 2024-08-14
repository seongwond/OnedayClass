package com.springboot.AoooA.service;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.AoooA.Entity.User;
import com.springboot.AoooA.Repository.UserRepository;
import com.springboot.AoooA.dto.MemberDTO;
import com.springboot.AoooA.dto.PasswordChangeDTO;

@Service
public class UserServiceImpl implements UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService ) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.emailService = emailService;
	}

	@Override
	public boolean changePassword(String userEmail, PasswordChangeDTO passwordChangeDto) {
		logger.info("비밀번호 변경 요청 받은 사용자 Email: {}", userEmail);

		// 사용자 정보 조회
		User user = userRepository.findByUserEmail(userEmail);

		// 디버깅을 위한 추가 로그
		if (user == null) {
			logger.error("사용자를 찾을 수 없음: {}", userEmail);
			throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
		}

		// 현재 비밀번호를 인코딩하여 저장된 비밀번호와 비교
		if (passwordEncoder.matches(passwordChangeDto.getCurrentPassword(), user.getUserPw())) {
			// 새로운 비밀번호를 인코딩하여 저장
			user.setUserPw(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
			userRepository.save(user);
			return true; // 비밀번호 변경 성공
		} else {
			return false; // 현재 비밀번호가 일치하지 않음
		}
	}

	@Override
	public User findByUserEmail(String userEmail) {
		return userRepository.findByUserEmail(userEmail);
	}

	public int updateUserInfo(MemberDTO memberDto) {
		User user = userRepository.findByUserEmail(memberDto.getUser_email());
		if (user == null) {
			throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
		}

		user.setUserNickname(memberDto.getUser_nickname());
		user.setUserTel(memberDto.getUser_tel());
		user.setUserPostcode(memberDto.getUser_postcode());
		user.setUserAddress1(memberDto.getUser_address1());
		user.setUserAddress2(memberDto.getUser_address2());
		user.setUserAddress3(memberDto.getUser_address3());

		userRepository.save(user);
		return 1; // 성공적으로 업데이트 된 행의 수 반환
	}

	@Override
	public String generateTemporaryPassword() {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder sb = new StringBuilder(8);
		Random random = new Random();
		for (int i = 0; i < 8; i++) {
			sb.append(characters.charAt(random.nextInt(characters.length())));
		}

		return sb.toString();
	}

	@Override
	public void sendTemporaryPassword(String userEmail) {
		User user = userRepository.findByUserEmail(userEmail);
		if (user != null) {
			String temporaryPassword = generateTemporaryPassword();
			logger.info("Generated Temporary Password for " + userEmail + ": " + temporaryPassword);

			changePassword1(userEmail, temporaryPassword); // 비밀번호를 임시 비밀번호로 변경
			System.out.println("왜 안 되냐고");
			if (userEmail != null) {
				emailService.sendTemporaryPassword(userEmail, temporaryPassword); // 임시 비밀번호를 이메일로 전송
			} else {
				logger.error("User email is null");
			}
		} else {
			logger.warn("No user found with email: " + userEmail);
		}
	}

	@Override
	public boolean changePassword1(String userEmail, String temporaryPassword) {
		logger.info("임시 비밀번호로 비밀번호 변경 요청 받은 사용자 Email: {}", userEmail);

		// 사용자 정보 조회
		User user = userRepository.findByUserEmail(userEmail);

		// 디버깅을 위한 추가 로그
		if (user == null) {
			logger.error("사용자를 찾을 수 없음: {}", userEmail);
			throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
		}

		// 새로운 임시 비밀번호로 비밀번호 변경
		user.setUserPw(passwordEncoder.encode(temporaryPassword));
		userRepository.save(user);
		return true; // 비밀번호 변경 성공
	}

}