package com.springboot.AoooA.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.AoooA.dao.MemberDAO;
import com.springboot.AoooA.dto.MemberDTO;
import com.springboot.AoooA.role.UserRole;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
	private final MemberDAO memberdao;
	private final PasswordEncoder passwordEncoder;
	
	public int findMaxUserId() throws DataAccessException {
		return memberdao.findMaxUserId();
	}

	// 유저 기본정보 인서트
	public MemberDTO insertUserInfo(MemberDTO dto) {
	    dto.setUser_email(dto.getUser_email());
		dto.setUser_pw(passwordEncoder.encode(dto.getUser_pw()));
		dto.setUser_name(dto.getUser_name());
		dto.setUser_nickname(dto.getUser_nickname());
		dto.setUser_postcode(dto.getUser_postcode());
		dto.setUser_address1(dto.getUser_address1());
		dto.setUser_address2(dto.getUser_address2());
		dto.setUser_address3(dto.getUser_address3());
		dto.setUser_gender(dto.getUser_gender());
		dto.setUser_age(dto.getUser_age());
		dto.setUser_tel(dto.getUser_tel());
		dto.setUser_type(dto.getUser_type());
		this.memberdao.insertUser(dto);
		return dto;
	}
	
	public boolean isUserIdExists(String userEmail) {
	    MemberDTO existingUser = memberdao.selectUserEmail(userEmail);
	    return existingUser != null;
	}

	public MemberDTO insertInterestInfo(MemberDTO dto) {
	    this.memberdao.insertInterest(dto);
	    return dto;
	}

	// 사업자 기본정보 인서트
	public MemberDTO insertBisInfo(MemberDTO dto) {
		dto.setUser_email(dto.getUser_email());
		dto.setUser_pw(passwordEncoder.encode(dto.getUser_pw()));
		dto.setUser_name(dto.getUser_name());
		dto.setUser_tel(dto.getUser_tel());
		dto.setUser_type(dto.getUser_type());
		this.memberdao.insertBis(dto);
		return dto;
	}
	
	public void deleteUserInfo(Integer user_id) {
		memberdao.deleteUserInfo(user_id);
	}

	@Override
	public UserDetails loadUserByUsername(String user_email) throws UsernameNotFoundException {
		MemberDTO member = this.memberdao.selectUserEmail(user_email);
		if (member == null) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}
		List<GrantedAuthority> authorities = new ArrayList<>();
		if ("admin".equals(user_email)) {
			authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
		} else {
			authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
		}
		return new User(member.getUser_email(), member.getUser_pw(), authorities);
	}
}