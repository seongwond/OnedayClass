package com.springboot.AoooA.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.dao.DataAccessException;

import com.springboot.AoooA.dto.ClassDTO;
import com.springboot.AoooA.dto.HeartDTO;
import com.springboot.AoooA.dto.ImgDTO;

@Mapper
public interface HeartDAO {
   @Select("select * from heart")
   public List<HeartDTO> selectAllHeart() throws DataAccessException;

   @Select("select * from class")
   public List<ClassDTO> selectAllClass() throws DataAccessException;

   @Select("select class_id from class where user_id = #{user_id} AND class_heart = '1'")
   public List<ClassDTO> getHeartClassId();

   @Select("select class_title from class where class_id = #{class_id}")
   public List<ClassDTO> selectClassHeart(Integer class_id) throws DataAccessException;

   // 클래스 아이디에 맞는 대표 이미지 가져오기
   @Select("select * from img where class_id = #{class_id} AND img_type = '0'")
   public List<ImgDTO> getAllHeartList(Integer classId) throws DataAccessException;

   // 하트테이블에 다 집어 넣기
   @Insert("INSERT INTO heart (heart_id, class_id, user_id, class_heart)"
         + " VALUES (#{heart_id}, #{class_id}, #{user_id}, #{class_heart})")
   public int insertHeart(HeartDTO heartDTO) throws DataAccessException;

   // 취소하면 하트테이블 삭제
   @Delete("delete from heart where class_id = #{class_id} AND user_id = #{user_id}")
   public int deleteHeart(HeartDTO heartDTO) throws DataAccessException;

   @Select("select user_id from user where user_email = #{user_email}")
   public int selectUserId(String userEmail);

   // 좋아요 업데이트
   @Update("update heart set class_heart = class_heart + 1 where user_id = #{user_id} and class_heart = '0'")
   public int updateHeart(Integer user_id) throws DataAccessException;

   // 하트한 클래스만 찾기
   @Select("SELECT DISTINCT class_id FROM heart WHERE user_id = #{user_id} AND class_heart = '1'")
   public List<Integer> selectMyHeartClassIds(Integer user_id);

   // 하트한 클래스의 타이틀을 가져오기
   @Select("SELECT class_title FROM class WHERE class_id = #{class_id}")
   ImgDTO selectClasstitle(Integer classId);

   @Select("SELECT COUNT(*) FROM heart WHERE user_id = #{user_id} AND class_id = #{class_id}")
   int countUserHeart(HeartDTO heartDTO);

   @Select("SELECT * FROM heart WHERE user_id = #{userId}")
   public List<HeartDTO> getUserHearts(int userId) throws DataAccessException;

}