package com.springboot.AoooA.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HeartDTO {
   private int heart_id;
   private int user_id;
   private int class_id;
   private int class_heart;
}