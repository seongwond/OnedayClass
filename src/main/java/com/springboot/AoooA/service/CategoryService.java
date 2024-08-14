package com.springboot.AoooA.service;

import java.util.List;

import com.springboot.AoooA.dto.CategoryDTO;
import com.springboot.AoooA.dto.ImgDTO;

public interface CategoryService {

	List<CategoryDTO> getAllCategory();

	List<ImgDTO> getClassDetails(String keyword, String sortType, int offset, int size);

	List<ImgDTO> getClassByArea(String area, String keyword, String sortType, int offset, int size);

	List<ImgDTO> getClassByType(Integer class_type, String keyword, String sortType, int offset, int size);

	int getTotalClassCountByKeyword(String keyword);

	List<ImgDTO> getClassByAreaAndKeyword(String area, String keyword, String sortType, int offset, int size);

	List<ImgDTO> getClassByTypeAndKeyword(Integer class_type, String keyword, String sortType, int offset, int size);

	int getTotalClassCountByAreaAndKeyword(String area, String keyword);

	int getTotalClassCountByTypeAndKeyword(Integer class_type, String keyword);

	ImgDTO getClassTitles(Integer classId);
}
