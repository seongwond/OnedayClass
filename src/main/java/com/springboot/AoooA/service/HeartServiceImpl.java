package com.springboot.AoooA.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.springboot.AoooA.dao.ClassDAO;
import com.springboot.AoooA.dao.HeartDAO;
import com.springboot.AoooA.dto.ClassDTO;
import com.springboot.AoooA.dto.HeartDTO;
import com.springboot.AoooA.dto.ImgDTO;

@Service
public class HeartServiceImpl implements HeartService {
   private final HeartDAO heartDAO;

   @Autowired
   public HeartServiceImpl(HeartDAO heartDAO, ClassDAO classDAO) {
      this.heartDAO = heartDAO;
   }

   @Override
   public List<HeartDTO> selectAllHeart() throws DataAccessException {
      return heartDAO.selectAllHeart();
   }

   @Override
   public List<ClassDTO> selectAllClass() throws DataAccessException {
      return heartDAO.selectAllClass();
   }

   @Override
   public List<ClassDTO> getHeartClassId() {
      return heartDAO.getHeartClassId();
   }

   @Override
   public int insertHeart(HeartDTO heartDTO) throws DataAccessException {
      return heartDAO.insertHeart(heartDTO);
   }

   @Override
   public int deleteHeart(HeartDTO heartDTO) throws DataAccessException {
      return heartDAO.deleteHeart(heartDTO);
   }

   @Override
   public int selectUserId(String userEmail) {
      return heartDAO.selectUserId(userEmail);
   }

   @Override
   public int updateHeart(Integer user_id) throws DataAccessException {
      return heartDAO.updateHeart(user_id);
   }
   
   @Override
   public List<Integer> getMyLikedClassIds(Integer userId) {
       return heartDAO.selectMyHeartClassIds(userId);
   }

   @Override
   public List<ImgDTO> getMyHeartClassDetails(Integer user_id) {
       // 사용자가 좋아요한 클래스 목록 가져오기
       List<Integer> classIds = heartDAO.selectMyHeartClassIds(user_id);
       List<ImgDTO> details = new ArrayList<>();

       // 각 클래스의 이미지 및 제목 가져오기
       for (Integer classId : classIds) {
           // 클래스의 이미지 정보 가져오기
           List<ImgDTO> images = heartDAO.getAllHeartList(classId);

           // 클래스의 제목 정보 가져오기
           ImgDTO classInfo = heartDAO.selectClasstitle(classId);

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

           details.add(detail);
       }

       return details;
   }

   
   @Override
   public List<HeartDTO> getUserHearts(int userId) throws DataAccessException {
       return heartDAO.getUserHearts(userId);
   }
   
   @Override
   public boolean isLiked(HeartDTO heartDTO) {
       int count = heartDAO.countUserHeart(heartDTO);
       return count > 0;
   }

   public boolean toggleHeart(HeartDTO heartDTO, boolean isLiked) throws DataAccessException {
       if (isLiked) {
           heartDAO.deleteHeart(heartDTO);
           return false;
       } else {
           heartDAO.insertHeart(heartDTO);
           return true;
       }
   }

}

