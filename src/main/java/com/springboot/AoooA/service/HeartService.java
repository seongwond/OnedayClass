package com.springboot.AoooA.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.springboot.AoooA.dto.ClassDTO;
import com.springboot.AoooA.dto.HeartDTO;
import com.springboot.AoooA.dto.ImgDTO;

public interface HeartService {

   List<HeartDTO> selectAllHeart() throws DataAccessException;

   List<ClassDTO> selectAllClass() throws DataAccessException;

   List<ClassDTO> getHeartClassId();

   int insertHeart(HeartDTO heartDTO) throws DataAccessException;

   int deleteHeart(HeartDTO heartDTO) throws DataAccessException;

   int selectUserId(String userEmail);

   int updateHeart(Integer user_id) throws DataAccessException;

   List<ImgDTO> getMyHeartClassDetails(Integer user_id);

   List<HeartDTO> getUserHearts(int userId) throws DataAccessException;
   
   boolean isLiked(HeartDTO heartDTO);
   
   List<Integer> getMyLikedClassIds(Integer userId);

}