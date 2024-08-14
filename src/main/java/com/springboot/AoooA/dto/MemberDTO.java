package com.springboot.AoooA.dto;

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
public class MemberDTO {
	
	private int user_id; // 키값
	private int interest_id;
	private int user_type;
	private String user_email; // 실질적 로그인 아이디
	private String user_pw;
	private String user_name;
	private String user_nickname;
	private String user_tel ;
	private String user_postcode;
	private String user_address1;
	private String user_address2;
	private String user_address3;
	private String interest_type1;
	private String interest_type2;
	private String interest_type3;
	private int user_age;
	private String user_gender;
}
