package com.springboot.AoooA.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.dao.DataAccessException;

import com.springboot.AoooA.dto.ImgDTO;
import com.springboot.AoooA.dto.ReservationDTO;
import com.springboot.AoooA.dto.ReviewDTO;

@Mapper
public interface ReviewDAO {

   // 기존 코드
   @Select("SELECT class_id, reservation_date FROM reservation WHERE user_id = #{user_id}")
   public List<Map<String, Object>> selectReview(Integer userId) throws DataAccessException;
   @Select("SELECT class_id, reservation_date FROM reservation WHERE user_id = #{user_id} AND reservation_id = #{reservation_id}")
   public List<ReservationDTO> selectReservationClassIds(@Param("user_id") Integer user_id,
         @Param("reservation_id") Integer reservation_id) throws DataAccessException;
   @Select("SELECT * FROM reservation where reservation_id = #{reservation_id}")
   public Integer selectReservationId(Integer reservation_id);
   @Select("SELECT class_id FROM reservation where user_id = #{user_id} and reservation_id = #{reservation_id}")
   public Integer selectClassId(Integer user_id, Integer reservation_id);

   
   // 수정된 코드 부분
   // 1. 모든 예약 정보 가져오기
   @Select("SELECT * FROM reservation")
   public List<ReservationDTO> selectAllReservation() throws DataAccessException;
   
   // 2. 클래스 아이디에 맞는 대표 이미지 가져오기
   @Select("SELECT * FROM img WHERE class_id = #{classId} AND img_type = '0'")
   public List<ImgDTO> getAllReviewList(Integer classId) throws DataAccessException;
   
   // 3. 클래스 제목 가져오기
   @Select("SELECT class_title FROM class WHERE class_id = #{classId}")
   ImgDTO selectClasstitle(Integer classId);
   
   // 4. 특정 사용자의 예약 정보 가져오기
   @Select("SELECT class_id, reservation_date, reservation_id FROM reservation WHERE user_id = #{userId}")
   public List<Map<String, Object>> selectReservationDate3(Integer userId) throws DataAccessException;
   
   // 5. 특정 예약 정보 가져오기
   @Select("SELECT reservation_id, class_id, reservation_date FROM reservation WHERE reservation_id = #{reservation_id}")
   ReviewDTO getReservationById(Integer reservation_id);
   
   // 6. 특정 사용자와 클래스의 예약 카운트 가져오기 (리뷰 작성 여부 확인용)
   @Select("SELECT COUNT(*) FROM review WHERE user_id = #{userId} AND class_id = #{classId}")
   int countReservationByUserAndClass(@Param("userId") Integer userId, @Param("classId") Integer classId);
   
   // 7. 특정 클래스의 리뷰 가져오기
   @Select("SELECT * FROM review WHERE class_id = #{classId}")
   public List<ReviewDTO> getReview(Integer classId);
   
   // 8. 특정 예약의 예약 날짜 가져오기
   @Select("SELECT reservation_date FROM reservation WHERE reservation_id = #{reservation_id}")
   public String getReservationDate(Integer reservation_id);
   
   // 9. 특정 사용자의 닉네임 가져오기
   @Select("SELECT user_nickname FROM user WHERE user_id = #{userId}")
   public String getUserNickname(Integer userId);
   
   // 10. 이메일로 사용자 아이디 가져오기
   @Select("SELECT user_id FROM user WHERE user_email = #{userEmail}")
   public int selectUserId(String userEmail);
   
   // 11. 리뷰 삽입
   @Insert("INSERT INTO review (user_id, class_id, class_star, reservation_id, review_content) VALUES (#{user_id}, #{class_id}, #{class_star}, #{reservation_id}, #{review_content})")
   public void insertReview(ReviewDTO reviewDTO) throws DataAccessException;
}