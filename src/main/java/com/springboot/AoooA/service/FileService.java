package com.springboot.AoooA.service;

import org.springframework.stereotype.Service;

import com.springboot.AoooA.dao.FileDAO;
import com.springboot.AoooA.dto.ImgDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {
	private final FileDAO fileDAO;

	public void insertFile(ImgDTO imgDTO) {
		fileDAO.insertFile(imgDTO);
	}

	public ImgDTO getFilename(int img_id) {
		return fileDAO.selectFileNamedById(img_id);
	}
}