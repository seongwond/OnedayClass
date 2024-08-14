package com.springboot.AoooA.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.springboot.AoooA.Entity.Reservation;
import com.springboot.AoooA.dto.ImgDTO;
import com.springboot.AoooA.dto.ReservationDTO;

import jakarta.transaction.Transactional;

@Service
@Transactional
public interface ReservationService {
   void insertRv(Reservation reservation) throws DataAccessException;

   void saveReservation(Reservation reservation);

   int getNextNo();
   
   List<ImgDTO> getReservationClassDetailsx(Integer user_id);

   List<ImgDTO> getReservationClassDetails(Integer user_id);

   int selectUserId(String userEmail);

   int selectUserId22(String userEmail);

   public Map<Integer, String> selectReservationDate2(Integer userId);

   public Map<Integer, Map<String, Object>> selectReservationDate(Integer userId);

   // 예약 내역 삭제
   void deleteReservationById(Integer reservation_id);

   // bisPage2전용
   int selectViewUserId(String userEmail);

   // bisPage2전용
   List<ImgDTO> getReservationClassDetailss(Integer user_id);

   List<ReservationDTO> selectViewthis(Integer user_id);

   int selectClassId(Integer userId);

   int selectClassIdfromClass(Integer userId);

   // ImgDTO getReservationClassDetailsss(Integer classId);

   // ReservationDTO selectClassEdit(String user_email);

   ReservationDTO getClassList(Integer classId) throws DataAccessException;

   void saveReservation(ReservationDTO reservationDTO);

   public Map<Integer, Map<String, Object>> selectReservationDate3(Integer userId);

   String selectImgname(Integer class_id);


   int updateReservationDates() throws DataAccessException;

   LocalDate selectNewReservationDate(int class_id, int userId);


boolean isClassAlreadyReserved(Integer userId, Integer classId, String reservation_date);

int selectUsertype(int userId);
}