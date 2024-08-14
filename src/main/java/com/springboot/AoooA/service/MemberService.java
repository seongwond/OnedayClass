package com.springboot.AoooA.service;

import java.util.List;

import com.springboot.AoooA.dto.*;

public interface MemberService {
	List<MemberDTO> getAllMember();
	
	List<MemberDTO> getIdMember(String user_email);
	
	void updatePage(MemberDTO userForm);
	
	MemberDTO viewInterest(String user_email);
	
	void updateInterest(String userEmail, MemberDTO dto);
	
	MemberDTO selectUserEmail(String userEmail);
}
