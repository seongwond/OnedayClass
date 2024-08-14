package com.springboot.AoooA.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.dao.DataAccessException;

import com.springboot.AoooA.dto.ClassDTO;
import com.springboot.AoooA.dto.ImgDTO;

@Mapper
public interface ClassDAO {
	// class_id max값 찾기
	@Select("SELECT COALESCE(MAX(class_id), 0) FROM class")
	public int findMaxClassId() throws DataAccessException;

	// class테이블 값 인서트
	@Insert("INSERT INTO class (class_id, user_id, class_title, class_category, class_type, class_intro, class_start, class_finish, class_time, class_regdate, class_maxpeople, class_cost, bis_postcode, bis_address1, bis_address2, bis_address3, bis_num, class_name)"
			+ " VALUES (#{class_id}, #{user_id}, #{class_title}, #{class_category}, #{class_type}, #{class_intro}, #{class_start}, #{class_finish}, #{class_time}, #{class_regdate}, #{class_maxpeople}, #{class_cost}, #{bis_postcode}, #{bis_address1}, #{bis_address2}, #{bis_address3}, #{bis_num}, #{class_name})")
	public void insertClass(ClassDTO classDTO) throws DataAccessException;

	// img테이블 값 인서트
	@Insert("INSERT INTO img (user_id, class_id, img_path, img_name, img_type) VALUES (#{user_id}, #{class_id}, #{img_path}, #{img_name}, #{img_type})")
	void insertImg(ImgDTO imgDTO) throws DataAccessException;

	@Select("select * from class where class_id = #{class_id}")
	public ClassDTO getClass(Integer classId) throws DataAccessException;

	@Select("select * from img where class_id = #{class_id}")
	public List<ImgDTO> getImg(Integer classId) throws DataAccessException;

	@Select("SELECT \r\n"
			+ "    c.class_start, \r\n"
			+ "    c.class_finish, \r\n"
			+ "    c.class_time, \r\n"
			+ "    c.class_maxpeople, \r\n"
			+ "    COALESCE(SUM(r.reservation_people), 0) AS total_reservation_people\r\n"
			+ "FROM \r\n"
			+ "    class c\r\n"
			+ "LEFT JOIN \r\n"
			+ "    reservation r ON c.class_id = r.class_id\r\n"
			+ "WHERE \r\n"
			+ "    c.class_id = #{class_id}\r\n"
			+ "GROUP BY \r\n"
			+ "    c.class_start, \r\n"
			+ "    c.class_finish, \r\n"
			+ "    c.class_time, \r\n"
			+ "    c.class_maxpeople;")
	public ClassDTO getReservation(Integer classId) throws DataAccessException;

// 좋아요 업데이트
	@Update("update class set class_heart = class_heart + 1 where class_id = #{class_id} and class_heart = '0'")
	public int upHeart(Integer classId) throws DataAccessException;

	// 좋아요 취소
	@Update("update class set class_heart = class_heart - 1 where class_id = #{class_id} and class_heart = '1'")
	public int downHeart(Integer classId) throws DataAccessException;

	@Select("SELECT class_id FROM class order by class_regdate desc")
	public List<Integer> selectTypeClassIds();

	
	@Select("SELECT class_id FROM class order by class_heart desc")
	public List<Integer> selectBestinfo();

	// 타입 카테고리
	@Select("SELECT * FROM class WHERE class_type = #{classType}")
	public List<Integer> selectAllTypeClass(Integer classType);

	// 지역 카테고리 찾기
	@Select("SELECT bis_address FROM class WHERE bis_address LIKE '%', #{area}, '%'")
	public String selectArea(@Param("area") String area) throws DataAccessException;

	// 지역별 클래스 목록 조회
	@Select("SELECT * FROM class WHERE bis_address LIKE CONCAT('%', #{area}, '%')")
	List<ClassDTO> selectClassesByArea(@Param("area") String area);

	@Select("SELECT user_id FROM user WHERE user_email = #{user_email}")
	int selectUserIdByUserEmail(String user_email) throws DataAccessException;

	@Select("SELECT class_id, class_category, class_type, class_title, class_intro, class_name, bis_postcode, bis_address1, bis_address2, bis_address3, bis_num, class_time, class_start, class_finish, class_maxpeople, class_cost, user_id FROM class WHERE user_id = #{user_id}")
	ClassDTO getClassInfoByUserId(Integer user_id) throws DataAccessException;

	@Select("SELECT img_id, img_path, img_name, img_type FROM img WHERE class_id = #{class_id}")
	List<ImgDTO> getImgInfo(Integer class_id) throws DataAccessException;

	@Update("UPDATE class SET class_category = #{class_category}, class_type = #{class_type}, class_title = #{class_title}, class_intro = #{class_intro}, class_name = #{class_name}, bis_postcode = #{bis_postcode}, bis_address1 = #{bis_address1}, bis_address2 = #{bis_address2}, bis_address3 = #{bis_address3}, bis_num = #{bis_num}, class_time = #{class_time}, class_start = #{class_start}, class_finish = #{class_finish}, class_maxpeople = #{class_maxpeople}, class_cost = #{class_cost} WHERE class_id = #{class_id}")
	int updateClassInfo(ClassDTO classDTO) throws DataAccessException;

	@Update("UPDATE img SET img_name = #{img_name}, img_path = #{img_path} WHERE img_id = #{img_id}")
	void updateImg(ImgDTO imgDTO) throws DataAccessException;
}