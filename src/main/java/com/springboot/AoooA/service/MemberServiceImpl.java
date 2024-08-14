package com.springboot.AoooA.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.AoooA.dao.MemberDAO;
import com.springboot.AoooA.dto.MemberDTO;

@Service
public class MemberServiceImpl implements MemberService {
	private final MemberDAO memberDAO;

	public MemberServiceImpl(MemberDAO memberDAO) {
		this.memberDAO = memberDAO;
	}

	@Override
	public List<MemberDTO> getAllMember() {
		List<MemberDTO> pList = memberDAO.selectAllMember();

		return pList;
	}

	@Override
	public List<MemberDTO> getIdMember(String user_email) {
		List<MemberDTO> pList = memberDAO.selectIdMember(user_email);

		return pList;
	}
	@Override
	public void updatePage(MemberDTO userForm) {
		memberDAO.insertPage(userForm);
	}
	
	@Override
	public MemberDTO viewInterest(String user_email) {
	    int user_id = memberDAO.selectUserIdByUserEmail(user_email);
	    return memberDAO.selectInterestByUserId(user_id);
	}
	
	@Override
	public void updateInterest(String user_email, MemberDTO dto) {
	    int user_id = memberDAO.selectUserIdByUserEmail(user_email);
	    System.out.println(user_id);
		System.out.println(dto.getInterest_type1());
		System.out.println(dto.getInterest_type2());
		System.out.println(dto.getInterest_type3());

        memberDAO.updateInterestByUserId(user_id, dto.getInterest_type1(), dto.getInterest_type2(), dto.getInterest_type3());
	}
	

	@Override
	public MemberDTO selectUserEmail(String userEamil) {
		MemberDTO pList = memberDAO.selectUserEmail(userEamil);

		return pList;
	}


}
