package com.springboot.AoooA.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.AoooA.dto.ClassDTO;
import com.springboot.AoooA.dto.ImgDTO;

public interface ClassService {
	int findMaxClassId() throws DataAccessException;

	void insertClass(ClassDTO classDTO) throws DataAccessException;

	void insertImg(ImgDTO imgDTO) throws DataAccessException;

	ClassDTO getClassList(Integer classId) throws DataAccessException;

	public Map<String, Object> getClassInfo(Integer classId) throws DataAccessException;

	int updateClassHeart(Integer classId) throws DataAccessException;

	int deleteClassHeart(Integer classId) throws DataAccessException;

	List<Integer> selectTypeClass();

	List<Integer> selectAllTypeClass(Integer classType);

	List<Integer> selectBestinfo();

	List<ClassDTO> getClassesByArea(String area);

	String selectArea(String keyword);

	ClassDTO selectClassEdit(String user_email);

	public void updateClassInfo(ClassDTO classDTO, ImgDTO imgDTO, MultipartFile mainImage, List<MultipartFile> subImages) throws IOException;

}