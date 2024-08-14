package com.springboot.AoooA.dto;

import java.time.LocalDate;

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
public class ImgDTO {
   private int img_id;
   private int class_id;
   private int user_id;
   private String class_title;
   private String class_cost;
   private String img_path;
   private String img_name;
   private int img_type;
   private String bis_address1;
   private String reservation_date;
   private LocalDate class_regdate;
   private String class_time;
   private String class_type;
}