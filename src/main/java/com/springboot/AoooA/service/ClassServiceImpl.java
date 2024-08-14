
package com.springboot.AoooA.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.AoooA.dao.ClassDAO;
import com.springboot.AoooA.dto.ClassDTO;
import com.springboot.AoooA.dto.ImgDTO;

@Service
public class ClassServiceImpl implements ClassService {
	private final ClassDAO classDAO;

	private static final String REPOSITORY_DIR = "C:\\Users\\Administrator\\Desktop\\AoooA\\AoooA\\src\\main\\resources\\static\\img\\";
	private static final String DEFAULT_IMAGE_DIR = "C:\\Users\\Administrator\\Desktop\\AoooA\\AoooA\\src\\main\\resources\\static\\img\\";

	@Autowired
	public ClassServiceImpl(ClassDAO classDAO) {
		this.classDAO = classDAO;
	}

	@Override
	public int findMaxClassId() throws DataAccessException {
		return classDAO.findMaxClassId();
	}

	@Override
	public void insertClass(ClassDTO classDTO) throws DataAccessException {
		classDTO.setClass_regdate(LocalDate.now());
		classDAO.insertClass(classDTO);
	}

	@Override
	public void insertImg(ImgDTO imgDTO) throws DataAccessException {
		classDAO.insertImg(imgDTO);
	}

	@Override
	public ClassDTO getClassList(Integer classId) throws DataAccessException {
		// 클래스 정보 가져오기
		ClassDTO classInfo = classDAO.getClass(classId);

		// 이미지 정보 목록 가져오기
		List<ImgDTO> imgInfos = classDAO.getImg(classId);

		// 이미지 정보 목록을 ClassDTO에 설정
		classInfo.setImgList(imgInfos);

		return classInfo;
	}

	@Override
	public Map<String, Object> getClassInfo(Integer classId) throws DataAccessException {
		ClassDTO dto = classDAO.getReservation(classId);
		Map<String, Object> response = new HashMap<>();
		// ClassInfo 객체에서 필요한 정보를 추출하여 Map 객체에 저장합니다.
		response.put("class_start", dto.getClass_start());
		response.put("class_finish", dto.getClass_finish());
		response.put("class_time", dto.getClass_time());
		response.put("class_maxpeople", dto.getClass_maxpeople());
		response.put("reservation_people", dto.getTotal_reservation_people());
		return response;
	}

	@Override
	public int updateClassHeart(Integer classId) throws DataAccessException {
		return classDAO.upHeart(classId);
	}

	@Override
	public int deleteClassHeart(Integer classId) throws DataAccessException {
		return classDAO.downHeart(classId);
	}

	@Override
	public List<Integer> selectTypeClass() {
		return classDAO.selectTypeClassIds();
	}

	@Override
	public List<Integer> selectAllTypeClass(Integer classType) {
		return classDAO.selectAllTypeClass(classType);
	}

	@Override
	public List<Integer> selectBestinfo() {
		return classDAO.selectBestinfo();
	}

	@Override
	public List<ClassDTO> getClassesByArea(String area) {
		return classDAO.selectClassesByArea(area);
	}

	@Override
	public String selectArea(String keyword) {
		// 1. 사용자가 입력한 지역 키워드(keyword)를 이용해 실제 DB 테이블의 컬럼 값을 가져옵니다.
		return classDAO.selectArea(keyword);
	}

	@Override
	public ClassDTO selectClassEdit(String user_email) {
		int user_id = classDAO.selectUserIdByUserEmail(user_email);
		ClassDTO class_info = classDAO.getClassInfoByUserId(user_id);
		List<ImgDTO> imgList = classDAO.getImgInfo(class_info.getClass_id());
		class_info.setImgList(imgList);
		class_info.setUser_id(user_id); // user_id 설정
		return class_info;
	}

	@Override
	@Transactional
	public void updateClassInfo(ClassDTO classDTO, ImgDTO imgDTO, MultipartFile mainImage,
			List<MultipartFile> subImages) throws IOException {
		// 클래스 정보 업데이트
		classDAO.updateClassInfo(classDTO);

		// 기존 이미지 정보 조회
		List<ImgDTO> existingImages = classDAO.getImgInfo(classDTO.getClass_id());

		// 메인 이미지 처리
		if (mainImage != null && !mainImage.isEmpty()) {
			updateOrInsertImage(mainImage, true, classDTO, imgDTO, existingImages, 0);
		}

		// 서브 이미지 처리
		for (int i = 0; i < subImages.size(); i++) {
			MultipartFile subImage = subImages.get(i);
			if (subImage != null && !subImage.isEmpty()) {
				updateOrInsertImage(subImage, false, classDTO, imgDTO, existingImages, i + 1);
			}
		}
	}

	private void updateOrInsertImage(MultipartFile file, boolean isMain, ClassDTO classDTO, ImgDTO imgDTO,
			List<ImgDTO> existingImages, int imgType) throws IOException {
		String prefix = isMain ? "main_" : "sub_";
		String fileName = prefix + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		file.transferTo(new File(REPOSITORY_DIR + fileName));

		imgDTO.setImg_name(fileName);
		imgDTO.setImg_path(REPOSITORY_DIR);
		imgDTO.setImg_type(imgType);

		boolean imageExists = false;

		// 기존 이미지가 있는지 확인
		for (ImgDTO existingImage : existingImages) {
			if (existingImage.getImg_type() == imgDTO.getImg_type()) {
				imgDTO.setUser_id(classDTO.getUser_id());
				imgDTO.setClass_id(classDTO.getClass_id());
				imgDTO.setImg_id(existingImage.getImg_id()); // 이미지 ID 설정
				classDAO.updateImg(imgDTO);
				imageExists = true;
				break;
			}
		}

		// 기존 이미지가 없는 경우 새로 삽입
		if (!imageExists) {
			imgDTO.setUser_id(classDTO.getUser_id());
			imgDTO.setClass_id(classDTO.getClass_id());
			classDAO.insertImg(imgDTO);
		}
	}

}