package com.springboot.AoooA.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.springboot.AoooA.dto.ImgDTO;
import com.springboot.AoooA.dto.ReservationDTO;
import com.springboot.AoooA.dto.ReviewDTO;

import jakarta.transaction.Transactional;

@Service
@Transactional
public interface ReviewService {

	// 기존 코드
	Map<Integer, Map<String, Object>> selectReservationDate(Integer userId);

	ImgDTO getClassInfo(Integer classId);

	List<ImgDTO> getMyReviewClass(Integer user_id, Integer reservation_id);

	Map<Integer, Map<String, Object>> selectReservationDate3(Integer userId);

	List<ImgDTO> getImagesByClassId(Integer classId);

	int selectUserId(String userEmail);

	int selectReservationId(Integer reservation_id);

	int selectClassId(Integer user_id, Integer reservation_id);

	List<ReviewDTO> getReview(Integer classId);

	// 수정된 코드 부분
	// 1.
	List<ReservationDTO> getAllReservations() throws DataAccessException;

	// 2.
	List<ImgDTO> getAllReviewList(Integer classId) throws DataAccessException;

	// 3.
	ImgDTO getClassTitle(Integer classId) throws DataAccessException;

	// 4.
	List<Map<String, Object>> getReservationDates(Integer userId) throws DataAccessException;

	// 5.
	ReviewDTO getReservationById(Integer reservation_id) throws DataAccessException;

	// 6.
	int countReservationByUserAndClass(Integer userId, Integer classId) throws DataAccessException;

	// 7.
	List<ReviewDTO> getReviewsByClassId(Integer classId) throws DataAccessException;

	// 8.
	String getReservationDate(Integer reservation_id) throws DataAccessException;

	// 9.
	String getUserNickname(Integer userId) throws DataAccessException;

	// 10.
	int getUserIdByEmail(String userEmail) throws DataAccessException;

	// 11.
	void insertReview(ReviewDTO reviewDTO) throws DataAccessException;
}