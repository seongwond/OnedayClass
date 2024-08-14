package com.springboot.AoooA.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.springboot.AoooA.Entity.Reservation;
import com.springboot.AoooA.Repository.ReservationRepository;
import com.springboot.AoooA.dao.ReservationDAO;
import com.springboot.AoooA.dto.ImgDTO;
import com.springboot.AoooA.dto.ReservationDTO;

@Service
public class ReservationServiceImpl implements ReservationService {
   @Autowired
   private ReservationDAO reservationDAO;

   @Autowired
   private ReservationRepository reservationRepository;

   @Override
   public void saveReservation(Reservation reservation) {
      reservationRepository.save(reservation);
   }

   @Override
   public void insertRv(Reservation reservation) {
      reservationRepository.save(reservation);
   }

   @Override
   public int getNextNo() {
      // 반환 값 문자열을 정수로 변환하여 반환
      return Integer.parseInt(reservationDAO.selectMaxNo()) + 1;
   }

   // 유저 이메일 기준으로 ID값 가져오기
   @Override
   public int selectUserId(String userEmail) {
      return reservationDAO.selectUserId(userEmail);
   }

   @Override
   public int selectUserId22(String userEmail) {
      return reservationDAO.selectUserId22(userEmail);
   }

   @Override
   public int selectClassId(Integer userId) {
      return reservationDAO.selectClassId(userId);
   }

   @Override
   public int selectClassIdfromClass(Integer userId) {
      return reservationDAO.selectClassIdfromClass(userId);
   }

   // bisPage2전용
   @Override
   public int selectViewUserId(String userEmail) {
      return reservationDAO.selectViewUserId(userEmail);
   }
   @Override
   public List<ImgDTO> getReservationClassDetailsx(Integer user_id) {
      // 1. 사용자의 관심 클래스 ID 목록 가져오기
      List<Integer> classIds = reservationDAO.selectNewx(user_id);

      List<ImgDTO> details = new ArrayList<>();

      // 2. 각 관심 클래스의 이미지 및 제목 정보 가져오기
      for (Integer classId : classIds) {
         // 2-1. 클래스의 이미지 정보 가져오기
         List<ImgDTO> images = reservationDAO.getAlReservationList(classId);

         // 2-2. 클래스의 제목 정보 가져오기
         ImgDTO classInfo = reservationDAO.selectClasstitle(classId);

         // 2-3. ImgDTO 객체 생성 및 정보 설정
         ImgDTO detail = new ImgDTO();
         if (!images.isEmpty()) {
            // 첫 번째 이미지 정보 설정
            ImgDTO firstImage = images.get(0);
            detail.setImg_id(firstImage.getImg_id());
            detail.setImg_path(firstImage.getImg_path());
            detail.setImg_name(firstImage.getImg_name());
            detail.setImg_type(firstImage.getImg_type());
         }
         detail.setClass_id(classId);
         detail.setClass_title(classInfo.getClass_title());

         details.add(detail);
      }

      return details;
   }
   @Override
   public List<ImgDTO> getReservationClassDetails(Integer user_id) {
      // 1. 사용자의 관심 클래스 ID 목록 가져오기
      List<Integer> classIds = reservationDAO.selectNew(user_id);

      List<ImgDTO> details = new ArrayList<>();

      // 2. 각 관심 클래스의 이미지 및 제목 정보 가져오기
      for (Integer classId : classIds) {
         // 2-1. 클래스의 이미지 정보 가져오기
         List<ImgDTO> images = reservationDAO.getAlReservationList(classId);

         // 2-2. 클래스의 제목 정보 가져오기
         ImgDTO classInfo = reservationDAO.selectClasstitle(classId);

         // 2-3. ImgDTO 객체 생성 및 정보 설정
         ImgDTO detail = new ImgDTO();
         if (!images.isEmpty()) {
            // 첫 번째 이미지 정보 설정
            ImgDTO firstImage = images.get(0);
            detail.setImg_id(firstImage.getImg_id());
            detail.setImg_path(firstImage.getImg_path());
            detail.setImg_name(firstImage.getImg_name());
            detail.setImg_type(firstImage.getImg_type());
         }
         detail.setClass_id(classId);
         detail.setClass_title(classInfo.getClass_title());

         details.add(detail);
      }

      return details;
   }

   public Map<Integer, String> selectReservationDate2(Integer userId) {
      List<Map<String, Object>> results = reservationDAO.selectReservationDate2(userId);
      Map<Integer, String> regDate = new HashMap<>();
      for (Map<String, Object> result : results) {
         Integer classId = (Integer) result.get("class_id");
         String registrationDate = (String) result.get("reservation_date");

         regDate.put(classId, registrationDate);
      }

      return regDate;
   }

   /*
    * public Map<Integer, Map<String, String>> selectReservationStart(Integer
    * userId) { List<Map<String, Object>> results =
    * reservationDAO.selectReservationStart(userId); Map<Integer, Map<String,
    * String>> regDateMap = new HashMap<>();
    * 
    * for (Map<String, Object> result : results) { Integer classId = (Integer)
    * result.get("class_id"); String registrationDate = (String)
    * result.get("reservation_date"); String registrationPrice = (String)
    * result.get("reservation_price");
    * 
    * Map<String, String> regDP = new HashMap<>(); regDP.put("reservation_date",
    * registrationDate); regDP.put("reservation_price", registrationPrice);
    * 
    * }
    * 
    * return regDateMap; }
    */
   // bisPage2전용
   @Override
   public List<ImgDTO> getReservationClassDetailss(Integer user_id) {
      // 1. 사용자의 관심 클래스 ID 목록 가져오기
      List<Integer> classIds = reservationDAO.selectViewClassIds(user_id);

      List<ImgDTO> details = new ArrayList<>();

      // 2. 각 관심 클래스의 이미지 및 제목 정보 가져오기
      for (Integer classId : classIds) {
         // 2-1. 클래스의 이미지 정보 가져오기
         List<ImgDTO> images = reservationDAO.getAlReservationList(classId);

         // 2-2. 클래스의 제목 정보 가져오기
         ImgDTO classInfo = reservationDAO.selectClasstitle(classId);

         // 2-3. ImgDTO 객체 생성 및 정보 설정
         ImgDTO detail = new ImgDTO();
         if (!images.isEmpty()) {
            // 첫 번째 이미지 정보 설정
            ImgDTO firstImage = images.get(0);
            detail.setImg_id(firstImage.getImg_id());
            detail.setImg_path(firstImage.getImg_path());
            detail.setImg_name(firstImage.getImg_name());
            detail.setImg_type(firstImage.getImg_type());
         }
         detail.setClass_id(classId);
         detail.setClass_title(classInfo.getClass_title());
         detail.setClass_cost(classInfo.getClass_cost());
         detail.setBis_address1(classInfo.getBis_address1());

         details.add(detail);
      }

      return details;
   }

   @Override
   public List<ReservationDTO> selectViewthis(Integer classId) {
      List<ReservationDTO> pList = reservationDAO.selectViewthis(classId);

      // Java Streams API를 사용하여 user_name 필드 설정
      pList.stream().forEach(dto -> dto.setUser_name(reservationDAO.selectViewName(dto.getUser_id())));

      return pList;
   }

   @Override
   public Map<Integer, Map<String, Object>> selectReservationDate(Integer userId) {
      List<Map<String, Object>> results = reservationDAO.selectReservationDate(userId);
      Map<Integer, Map<String, Object>> regDateMap = new HashMap<>();

      for (Map<String, Object> result : results) {
         Integer classId = (Integer) result.get("class_id");
//            String reservationDate = (String) result.get("reservation_date");
//            String reservationPrice = (String) result.get("reservation_price");

         // 내부 맵 생성
         Map<String, Object> reservationDetails = new HashMap<>();
         reservationDetails.put("reservation_date", result.get("reservation_date"));
         reservationDetails.put("reservation_price", result.get("reservation_price"));
         reservationDetails.put("reservation_id", result.get("reservation_id"));

         regDateMap.put(classId, reservationDetails);
      }
      return regDateMap;
   }

// 나의 에약 내역 삭제
   @Override
   public void deleteReservationById(Integer reservation_id) {
      reservationDAO.deleteReservationById(reservation_id);
   }

   /*
    * @Override public ReservationDTO selectClassEdit(String user_email) { int
    * user_id = reservationDAO.selectUserIdByUserEmail(user_email); ReservationDTO
    * class_info = reservationDAO.getClassInfoByUserId(user_id); // class_id를 이용하여
    * 이미지 정보를 가져오는 로직 추가 List<ImgDTO> imgList =
    * reservationDAO.getImgInfo(class_info.getClass_id());
    * class_info.setImgList(imgList); // ClassDTO에 이미지 리스트 설정 return class_info; }
    */
   @Override
   public ReservationDTO getClassList(Integer classId) throws DataAccessException {
      // 클래스 정보 가져오기
      ReservationDTO classInfo = reservationDAO.getClass(classId);

      // 이미지 정보 목록 가져오기
      List<ImgDTO> imgInfos = reservationDAO.getImg(classId);

      // 이미지 정보 목록을 ClassDTO에 설정
      classInfo.setImgList(imgInfos);

      return classInfo;
   }

   public void saveReservation(ReservationDTO reservationDTO) {
      reservationDAO.insertReservation(reservationDTO);
   }

   public Map<Integer, Map<String, Object>> selectReservationDate3(Integer userId) {
      List<Map<String, Object>> results = reservationDAO.selectReservationDate3(userId);
      Map<Integer, Map<String, Object>> regDateMap = new HashMap<>();
      for (Map<String, Object> result : results) {
         Integer classId = (Integer) result.get("class_id");

         Map<String, Object> reservationDetails = new HashMap<>();
         reservationDetails.put("reservation_date", result.get("reservation_date"));
         reservationDetails.put("reservation_id", result.get("reservation_id"));

         regDateMap.put(classId, reservationDetails);
      }
      return regDateMap;
   }

   @Override
   public String selectImgname(Integer class_id) {
      return reservationDAO.selectImgname(class_id);
   }

   @Override
   public boolean isClassAlreadyReserved(Integer userId, Integer classId, String reservation_date) {
      return reservationDAO.countUserReservationsForClass(userId, classId, reservation_date) > 0;
   }

    @Override
    public int updateReservationDates() throws DataAccessException {
       return reservationDAO.updateReservationDates();
    }
   
   public LocalDate selectNewReservationDate(int class_id , int userId) {
      return reservationDAO.selectNewReservationDate(class_id, userId);
   }
   
   public LocalDate selectNewReservationDates(int class_id , int userId) {
	      return reservationDAO.selectNewReservationDates(class_id, userId);
	   }
   
   @Override
   public int selectUsertype(int userId) {
      return reservationDAO.selectUsertype(userId);
   }

}