package com.springboot.AoooA.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.AoooA.dao.CategoryDAO;
import com.springboot.AoooA.dto.CategoryDTO;
import com.springboot.AoooA.dto.ImgDTO;

@Service
public class CategoryServiceImpl implements CategoryService {
	private final CategoryDAO categoryDAO;

	@Autowired
	public CategoryServiceImpl(CategoryDAO categoryDAO) {
		this.categoryDAO = categoryDAO;
	}

	public List<CategoryDTO> getAllCategory() {
		List<CategoryDTO> list = this.categoryDAO.selectAllCategory();
		return list;
	}

	@Override
	public List<ImgDTO> getClassDetails(String keyword, String sortType, int offset, int size) {
		return categoryDAO.selectCategoryByTitleAndSort(keyword, sortType, offset, size);
	}

	@Override
	public List<ImgDTO> getClassByArea(String area, String keyword, String sortType, int offset, int size) {
		return categoryDAO.selectCategoryByAreaAndKeyword(area, keyword, sortType, offset, size);
	}

	@Override
	public List<ImgDTO> getClassByType(Integer type, String keyword, String sortType, int offset, int size) {
		return categoryDAO.selectCategoryByTypeAndKeyword(type, keyword, sortType, offset, size);
	}

	@Override
	public int getTotalClassCountByKeyword(String keyword) {
		return categoryDAO.getTotalClassCountByKeyword(keyword);
	}

	@Override
	public List<ImgDTO> getClassByAreaAndKeyword(String area, String keyword, String sortType, int offset, int size) {
		return categoryDAO.selectCategoryByAreaAndKeyword(area, keyword, sortType, offset, size);
	}

	@Override
	public List<ImgDTO> getClassByTypeAndKeyword(Integer class_type, String keyword, String sortType, int offset,
			int size) {
		return categoryDAO.selectCategoryByTypeAndKeyword(class_type, keyword, sortType, offset, size);
	}

	@Override
	public int getTotalClassCountByAreaAndKeyword(String area, String keyword) {
		return categoryDAO.getTotalClassCountByAreaAndKeyword(area, keyword);
	}

	@Override
	public int getTotalClassCountByTypeAndKeyword(Integer class_type, String keyword) {
		return categoryDAO.getTotalClassCountByTypeAndKeyword(class_type, keyword);
	}


	@Override
	public ImgDTO getClassTitles(Integer classId) {
		// 가정: getAllList 메서드는 클래스에 속한 모든 이미지 정보를 반환합니다.
		List<ImgDTO> images = categoryDAO.getAllList(classId);
		// 가정: selectClassInfo 메서드는 클래스의 제목과 비용 정보를 포함한 ImgDTO를 반환합니다.
		ImgDTO classInfo = categoryDAO.selectClassData(classId);

		// 새로운 ImgDTO 객체 생성
		ImgDTO details = new ImgDTO();

		// 이미지 정보가 여러 개 있을 수 있으므로, 첫 번째 이미지의 정보를 사용하는 것으로 가정합니다.
		if (!images.isEmpty()) {
			// 첫 번째 이미지 정보 설정
			ImgDTO firstImage = images.get(0);
			details.setImg_id(firstImage.getImg_id());
			details.setImg_path(firstImage.getImg_path());
			details.setImg_name(firstImage.getImg_name());
			details.setImg_type(firstImage.getImg_type());
		}

		// 클래스 정보 설정
		if (classInfo != null) {
			details.setClass_id(classId); // 클래스 ID 설정
			details.setClass_title(classInfo.getClass_title());
		}

		return details;
	}

}
