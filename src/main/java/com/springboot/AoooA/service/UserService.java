package com.springboot.AoooA.service;

import com.springboot.AoooA.dto.MemberDTO;
import com.springboot.AoooA.dto.PasswordChangeDTO;
import com.springboot.AoooA.Entity.User;

public interface UserService {
	boolean changePassword(String userEmail, PasswordChangeDTO passwordChangeDTO);

	User findByUserEmail(String userEmail);

	int updateUserInfo(MemberDTO dto);

	String generateTemporaryPassword();

	boolean changePassword1(String userEmail, String temporaryPassword);

	void sendTemporaryPassword(String userEmail);
}