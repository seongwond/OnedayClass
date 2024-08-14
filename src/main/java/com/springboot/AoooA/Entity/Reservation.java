package com.springboot.AoooA.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "reservation")
@Getter
@Setter
@ToString
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int reservation_id;
	private int user_id;
	private int class_id;
	private int img_id;
	private String reservation_price;
	private String reservation_date;
	private String reservation_people;
	private String reservation_start;
	private String reservation_regnum;

	// 기본 생성자
	public Reservation() {
	}

	// 필요한 필드를 가진 생성자
	public Reservation(int reservationId, int userId, int classId, int imgId, String reservationPrice,
			String reservationDate, String reservationPeople, String reservationStart, String reservationRegnum) {
		this.reservation_id = reservationId;
		this.user_id = userId;
		this.class_id = classId;
		this.img_id = imgId;
		this.reservation_price = reservationPrice;
		this.reservation_date = reservationDate;
		this.reservation_people = reservationPeople;
		this.reservation_start = reservationStart;
		this.reservation_regnum = reservationRegnum;
	}

	public int getReservationId() {
		return this.reservation_id;
	}

	public int getUserId() {
		return this.user_id;
	}

	public String getReservationPrice() {
		return this.reservation_price;
	}

	public String getReservationDate() {
		return this.reservation_date;
	}

	public String getReservationPeople() {
		return this.reservation_people;
	}

	public String getReservationStart() {
		return this.reservation_start;
	}

	public String getReservationRegnum() {
		return this.reservation_regnum;
	}

}