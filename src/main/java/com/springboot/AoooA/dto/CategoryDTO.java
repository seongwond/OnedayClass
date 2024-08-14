package com.springboot.AoooA.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategoryDTO {
	private int type_id;
	private int class_id;
	private String class_title;
	private String class_category;
	private String class_type;
	private String keyword;
	private List<ImgDTO> imgList;
	
}