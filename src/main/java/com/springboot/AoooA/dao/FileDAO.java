package com.springboot.AoooA.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.springboot.AoooA.dto.ImgDTO;

@Mapper
public interface FileDAO {
	@Insert("insert into img(img_id, img_name, img_path, user_id, img_type)"
			+ "values(#{img_id}, #{img_name}, #{img_path}, #{user_id}, #{img_type})")
	public void insertFile(ImgDTO imgDTO);

	@Select("select img_name from img where img_id=#{img_id}")
	public ImgDTO selectFileNamedById(int img_id);

}
