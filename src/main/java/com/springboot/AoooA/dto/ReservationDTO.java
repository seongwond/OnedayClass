package com.springboot.AoooA.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class ReservationDTO {
	private int reservation_id;
	private int user_id;
	private int class_id;
	private int img_id;
	private String reservation_date;
	private String reservation_start;
	private String reservation_people;
	private String reservation_price;
	private String reservation_regnum;
	private String bis_address1;
	private String class_title;
	private String user_name;
	private List<ImgDTO> imgList;
	private String img_link;
	private LocalDate formattedDate; // 추가된 필드
	private LocalDate new_reservation_date;
}