package com.springboot.AoooA.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.AoooA.Entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

}