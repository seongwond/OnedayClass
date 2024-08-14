package com.springboot.AoooA.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClassDTO {
	private int class_id;
	private int user_id;
	private int review_id;
	private String class_title;
	private String class_category;
	private String class_intro;
	private String class_content;
	private String class_type;
	private String class_curi;
	private String class_calender;
	private String class_start;
	private String class_finish;
	private String class_time;
	private String class_maxpeople;
	private String class_cost;
	private String class_view;
	private LocalDate class_regdate;
	private String class_name;
	private String bis_postcode;
	private String bis_address1;
	private String bis_address2;
	private String bis_address3;
	private String bis_num;
	private String class_best;
	private String class_heart;
	private List<ImgDTO> imgList;
	private String keyword;
	private String reservation_people;
	private String total_reservation_people; 
}