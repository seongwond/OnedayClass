package com.springboot.AoooA.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReviewDTO {

	private Integer review_id;
	private Integer user_id;
	private Integer class_id;
	private Integer class_star;
	private String review_content;
	private Integer reservation_id;

	private String user_nickname;

	private String reservation_date;
}