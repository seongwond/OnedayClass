package com.springboot.AoooA.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.dao.DataAccessException;

import com.springboot.AoooA.dto.MemberDTO;

@Mapper
public interface MemberDAO {
	// user_id max값 찾기
	@Select("SELECT COALESCE(MAX(user_id), 0) FROM user")
	public int findMaxUserId() throws DataAccessException;

	// 유저 기본정보 인서트
	@Insert("insert into user(user_id, user_email, user_pw, user_name, user_nickname, user_postcode, user_address1, user_address2, user_address3, user_tel, user_type , user_gender, user_age) "
			+ "values(#{user_id}, #{user_email}, #{user_pw}, #{user_name}, #{user_nickname}, #{user_postcode}, #{user_address1}, #{user_address2}, #{user_address3}, #{user_tel}, #{user_type}, #{user_gender}, #{user_age})")
	public void insertUser(MemberDTO memberDTO) throws DataAccessException;

	// 유저 이메일 셀렉트
	@Select("select * from user where user_email = #{user_email}")
	public MemberDTO selectUserEmail(String userEmail) throws DataAccessException;

	// 유저 관심분야 인서트
	@Insert("insert into interest(user_id, interest_type1, interest_type2, interest_type3) "
			+ "values(#{user_id}, #{interest_type1}, #{interest_type2}, #{interest_type3})")
	public void insertInterest(MemberDTO memberDTO) throws DataAccessException;

	// 사업자 기본정보 인서트
	@Insert("insert into user(user_id, user_email, user_pw, user_name, user_tel, user_type) "
			+ "values(#{user_id}, #{user_email}, #{user_pw}, #{user_name}, #{user_tel}, #{user_type})")
	public void insertBis(MemberDTO memberDTO) throws DataAccessException;

	@Select("select * from user")
	public List<MemberDTO> selectAllMember() throws DataAccessException;

	@Select("select * from user where user_email=#{user_email}")
	public List<MemberDTO> selectIdMember(String user_email) throws DataAccessException;

	@Update("update user set user_pw=#{user_pw}, user_nickname = #{user_nickname} , user_name= #{user_name}, user_tel = #{user_tel} where user_email = #{user_email}")
	public void insertPage(MemberDTO userForm) throws DataAccessException;

	@Select("select user_id from user where user_email = #{user_email}")
	int selectUserIdByUserEmail(String user_email) throws DataAccessException;

	@Select("select interest_type1, interest_type2, interest_type3 from interest where user_id = #{user_id}")
	MemberDTO selectInterestByUserId(int user_id) throws DataAccessException;

	@Update("UPDATE interest SET interest_type1 = #{interest_type1}, interest_type2 = #{interest_type2}, interest_type3 = #{interest_type3} WHERE user_id = #{user_id}")
	void updateInterestByUserId(@Param("user_id") int user_id, @Param("interest_type1") String interest_type1,
			@Param("interest_type2") String interest_type2, @Param("interest_type3") String interest_type3)
			throws DataAccessException;

	@Update("update user set user_nickname = #{user_nickname}, user_tel = #{user_tel}, user_postcode = #{user_postcode}, user_address1 = #{user_address1}, user_address2 = #{user_address2}, user_address3 = #{user_address3} where user_email = #{user_email}")
	int updateUserInfo(MemberDTO dto) throws DataAccessException;

	@Delete("delete from user where user_id = #{user_id}")
	int deleteUserInfo(Integer user_id) throws DataAccessException;

}