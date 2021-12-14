package com.springboot.hotelbookingsystem.repositories;

import com.springboot.hotelbookingsystem.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}