package com.springboot.AoooA.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.springboot.AoooA.dao.ReviewDAO;
import com.springboot.AoooA.dto.ImgDTO;
import com.springboot.AoooA.dto.ReservationDTO;
import com.springboot.AoooA.dto.ReviewDTO;

@Service
public class ReviewServiceImpl implements ReviewService {
   private final ReviewDAO reviewDAO;

   @Autowired
   public ReviewServiceImpl(ReviewDAO reviewDAO) {
      this.reviewDAO = reviewDAO;
   }

   // 기존 코드
   @Override
   public Map<Integer, Map<String, Object>> selectReservationDate(Integer userId) {
      List<Map<String, Object>> results = reviewDAO.selectReview(userId);
      Map<Integer, Map<String, Object>> regDateMap = new HashMap<>();

      for (Map<String, Object> result : results) {
         Integer classId = (Integer) result.get("class_id");

         // 내부 맵 생성
         Map<String, Object> reservationDetails = new HashMap<>();
         reservationDetails.put("reservation_date", result.get("reservation_date"));

         regDateMap.put(classId, reservationDetails);
      }
      return regDateMap;
   }

   @Override
   public ImgDTO getClassInfo(Integer classId) {
      return reviewDAO.selectClasstitle(classId);
   }
   @Override
   public List<ImgDTO> getMyReviewClass(Integer user_id, Integer reservation_id) {
      // 사용자가 수강한 클래스 목록 가져오기
      List<ReservationDTO> reservations = reviewDAO.selectReservationClassIds(user_id, reservation_id);
      List<ImgDTO> details = new ArrayList<>();
      // 각 클래스의 이미지 및 제목 가져오기
      for (ReservationDTO reservation : reservations) {
         Integer classId = reservation.getClass_id();
         String reservationDate = reservation.getReservation_date();
         // 클래스의 이미지 정보 가져오기
         List<ImgDTO> images = reviewDAO.getAllReviewList(classId);
         // 클래스의 제목 정보 가져오기
         ImgDTO classInfo = reviewDAO.selectClasstitle(classId);
         // ImgDTO 객체 생성 및 정보 설정
         ImgDTO detail = new ImgDTO();
         if (!images.isEmpty()) {
            ImgDTO firstImage = images.get(0);
            detail.setImg_id(firstImage.getImg_id());
            detail.setImg_path(firstImage.getImg_path());
            detail.setImg_name(firstImage.getImg_name());
            detail.setImg_type(firstImage.getImg_type());
         }
         detail.setClass_id(classId);
         detail.setClass_title(classInfo.getClass_title());
         detail.setReservation_date(reservationDate); // 예약 날짜 설정
         details.add(detail);
      }
      return details;
   }
   @Override
   public Map<Integer, Map<String, Object>> selectReservationDate3(Integer userId) {
      List<Map<String, Object>> results = reviewDAO.selectReservationDate3(userId);
      Map<Integer, Map<String, Object>> regDateMap = new HashMap<>();
      for (Map<String, Object> result : results) {
         Integer classId = (Integer) result.get("class_id");
         // 내부 맵 생성
         Map<String, Object> reservationDetails = new HashMap<>();
         reservationDetails.put("reservation_date", result.get("reservation_date"));
         regDateMap.put(classId, reservationDetails);
      }
      return regDateMap;
   }

   // 유저 이메일 기준으로 ID값 가져오기
   @Override
   public int selectUserId(String userEmail) {
      return reviewDAO.selectUserId(userEmail);
   }
   @Override
   public List<ImgDTO> getImagesByClassId(Integer classId) {
      return reviewDAO.getAllReviewList(classId);
   }
   @Override
   public int selectReservationId(Integer reservation_id) {
      return reviewDAO.selectReservationId(reservation_id);
   }
   @Override
   public int selectClassId(Integer user_id, Integer reservation_id) {
      return reviewDAO.selectClassId(user_id, reservation_id);
   }
   @Override
   public List<ReviewDTO> getReview(Integer classId) {
       List<ReviewDTO> reviews = reviewDAO.getReview(classId);
       for (ReviewDTO review : reviews) {
           String reservationDate = reviewDAO.getReservationDate(review.getReservation_id());
           String userNickname = reviewDAO.getUserNickname(review.getUser_id());
           review.setReservation_date(reservationDate);
           review.setUser_nickname(userNickname);
       }
       return reviews;
   }

   
   // 수정된 코드 부분
   // 1.
   @Override
    public List<ReservationDTO> getAllReservations() throws DataAccessException {
        return reviewDAO.selectAllReservation();
    }
   // 2.
    @Override
    public List<ImgDTO> getAllReviewList(Integer classId) throws DataAccessException {
        return reviewDAO.getAllReviewList(classId);
    }
    // 3.
    @Override
    public ImgDTO getClassTitle(Integer classId) throws DataAccessException {
        return reviewDAO.selectClasstitle(classId);
    }
    // 4.
    @Override
    public List<Map<String, Object>> getReservationDates(Integer userId) throws DataAccessException {
        return reviewDAO.selectReservationDate3(userId);
    }
    // 5.
    @Override
    public ReviewDTO getReservationById(Integer reservation_id) throws DataAccessException {
        return reviewDAO.getReservationById(reservation_id);
    }
    // 6.
    @Override
    public int countReservationByUserAndClass(Integer userId, Integer classId) throws DataAccessException {
        return reviewDAO.countReservationByUserAndClass(userId, classId);
    }
    // 7.
    @Override
    public List<ReviewDTO> getReviewsByClassId(Integer classId) throws DataAccessException {
        return reviewDAO.getReview(classId);
    }
    // 8.
    @Override
    public String getReservationDate(Integer reservation_id) throws DataAccessException {
        return reviewDAO.getReservationDate(reservation_id);
    }
    // 9.
    @Override
    public String getUserNickname(Integer userId) throws DataAccessException {
        return reviewDAO.getUserNickname(userId);
    }
    // 10.
    @Override
    public int getUserIdByEmail(String userEmail) throws DataAccessException {
        return reviewDAO.selectUserId(userEmail);
    }
    // 11.
    @Override
    public void insertReview(ReviewDTO reviewDTO) throws DataAccessException {
       reviewDAO.insertReview(reviewDTO);
    }
  

}