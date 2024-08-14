package com.springboot.AoooA.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.dao.DataAccessException;

import com.springboot.AoooA.Entity.Reservation;
import com.springboot.AoooA.dto.ImgDTO;
import com.springboot.AoooA.dto.ReservationDTO;

@Mapper
public interface ReservationDAO {

   @Select("SELECT COALESCE(MAX(reservation_regnum), 20240000) FROM reservation")
   public String selectMaxNo() throws DataAccessException;

   @Insert("INSERT INTO reservation (reservation_id, user_id, class_id, img_id, reservation_date, reservation_start, reservation_people, reservation_price, reservation_regnum) VALUES (#{reservation_id}, #{user_id}, #{class_id}, #{img_id}, #{reservation_date}, #{reservation_start}, #{reservation_people}, #{reservation_price}, #{reservation_regnum})")
   public void insertRv(Reservation reservation) throws DataAccessException;

   // 유저 이메일을 기준으로 id 값 가져오기
   @Select("SELECT user_id FROM user where user_email = #{user_email}")
   public int selectUserId(String userEmail);

   @Select("SELECT class_id FROM class WHERE user_id = #{user_id}")
   public int selectClassId(Integer user_id);

   @Select("SELECT class_id FROM reservation WHERE user_id = #{user_id}")
   public int selectClassIdfromClass(Integer user_id);

   @Select("SELECT class_id FROM reservation WHERE user_id = #{user_id}")
   public List<Integer> selectReservationClassIds(Integer user_id);

   // 클래스 아이디에 맞는 대표 이미지 가져오기
   @Select("select * from img where class_id = #{class_id} AND img_type = '0'")
   public List<ImgDTO> getAlReservationList(Integer classId) throws DataAccessException;

   // 타이틀을 가져오기
   @Select("SELECT class_title, bis_address1 FROM class WHERE class_id = #{class_id}")
   ImgDTO selectClasstitle(Integer classId);

   @Select("SELECT class_id, reservation_date, reservation_start FROM reservation WHERE user_id = #{userId}")
   public List<Map<String, Object>> selectReservationDate2(Integer userId) throws DataAccessException;

   @Select("SELECT user_id FROM user where user_email = #{user_email}")
   public int selectViewUserId(String userEmail);

   @Select("SELECT class_id FROM class WHERE user_id = #{user_id}")
   public List<Integer> selectViewClassIds(Integer user_id);

   // bisPage2

   @Select("SELECT user_id, reservation_date, reservation_start, reservation_people, reservation_price FROM reservation where class_id = #{class_id} ORDER BY reservation_date desc;")
   public List<ReservationDTO> selectViewthis(Integer classId);

   @Select("SELECT user_name FROM user where user_id = #{user_id}")
   public String selectViewName(Integer user_id);

   // 클래스 아이디에 맞는 예약한 날짜 가져오기
   @Select("SELECT class_id, reservation_date, reservation_price, reservation_id FROM reservation WHERE user_id = #{user_id}")
   public List<Map<String, Object>> selectReservationDate(Integer userId) throws DataAccessException;

   // 예약 내역 삭제
   @Delete("DELETE FROM reservation WHERE reservation_id = #{reservation_id}")
   public void deleteReservationById(Integer reservation_id);

   @Select("SELECT class_id  FROM class WHERE class_id = #{class_id}")
   public List<Integer> selectViewClassIdss(Integer classId);

   //

   // 유저 이메일을 기준으로 id 값 가져오기
   @Select("SELECT user_id FROM user where user_email = #{userEmail}")
   public int selectUserId22(String userEmail);

   @Select("SELECT class_id FROM reservation WHERE user_id = #{user_id} and class_id=#{class_id}")
   public int selectClassIdfromClass2(Integer user_id, Integer class_id);

   @Select("select user_id from user where user_email = #{user_email}")
   public int selectUserIdByUserEmail(String user_email) throws DataAccessException;

   @Select("select class_id from reservation where user_id = ${user_id}")
   public ReservationDTO getClassInfoByUserId(Integer user_id) throws DataAccessException;

   @Select("select img_path, img_name, img_type from img where class_id = #{class_id}")
   public List<ImgDTO> getImgInfo(Integer class_id) throws DataAccessException;

   @Select("select * from img where class_id = #{class_id}")
   public List<ImgDTO> getImg(Integer classId) throws DataAccessException;

   @Select("select * from class where class_id = #{class_id}")
   public ReservationDTO getClass(Integer classId) throws DataAccessException;

   @Insert("INSERT INTO reservation (reservation_id, user_id, class_id, img_id, reservation_date, reservation_start, reservation_people, reservation_price, reservation_regnum) VALUES (#{reservation_id}, #{user_id}, #{class_id}, #{img_id}, #{reservation_date}, #{reservation_start}, #{reservation_people}, #{reservation_price}, #{reservation_regnum})")
   public void insertReservation(ReservationDTO reservationDTO) throws DataAccessException;

   @Select("SELECT class_id, reservation_date, reservation_start, reservation_id FROM reservation WHERE user_id = #{userId}")
   public List<Map<String, Object>> selectReservationDate3(Integer userId) throws DataAccessException;

   // 여기부터
   @Select("SELECT * FROM reservation WHERE reservation_id = #{reservation_id}")
   ReservationDTO selectReservationById(Integer reservationId) throws DataAccessException;

   // 기존 메소드
   @Select("SELECT class_title FROM class WHERE class_id = #{class_id}")
   ImgDTO selectClasstitles(Integer classId);

   @Select("SELECT * FROM img WHERE class_id = #{class_id} AND img_type = '0'")
   List<ImgDTO> getAllReviewList(Integer classId) throws DataAccessException;

   @Select("SELECT img_name FROM img where class_id = #{class_id} and img_type='0'")
   public String selectImgname(Integer class_id);

   // 예약확인
   @Select("SELECT COUNT(*) FROM reservation WHERE user_id = #{userId} AND class_id = #{classId} AND reservation_date = #{reservation_date}")
   int countUserReservationsForClass(@Param("userId") Integer userId, @Param("classId") Integer classId, @Param("reservation_date") String reservation_date);

   // 예약 날짜를 업데이트하는 메서드
   @Update("UPDATE reservation SET new_reservation_date = STR_TO_DATE(reservation_date, '%Y년 %m월%d일')")
   public int updateReservationDates() throws DataAccessException;

   @Select("SELECT new_reservation_date FROM reservation WHERE class_id=#{class_id} AND user_id=#{userId}")
   public LocalDate selectNewReservationDate(@Param("class_id") Integer class_id, @Param("userId") Integer userId);

   @Select("select class_id from reservation where new_reservation_date < date(now()) and user_id=#{user_id} order by new_reservation_date asc")
   public List<Integer> selectNew(Integer user_id) throws DataAccessException;
   
   @Select("select class_id from reservation where new_reservation_date >= date(now()) and user_id=#{user_id} order by new_reservation_date asc")
   public List<Integer> selectNewx(Integer user_id) throws DataAccessException;
   
   @Select("SELECT new_reservation_date FROM reservation WHERE class_id=#{class_id} AND user_id=#{userId} LIMIT 1")
   public LocalDate selectNewReservationDates(@Param("class_id") Integer class_id, @Param("userId") Integer userId);


   @Select("SELECT user_type FROM user WHERE user_id = #{userId}")
   public int selectUsertype(Integer userId);


}