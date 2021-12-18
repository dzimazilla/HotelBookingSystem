package com.springboot.hotelbookingsystem.repositories;


import com.springboot.hotelbookingsystem.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r FROM Reservation r WHERE r.user.id = ?1")
    List<Reservation> findByUserId(Long id);

    @Query("SELECT r FROM Reservation r WHERE r.room.id = ?1")
    List<Reservation> findByRoomId(Long id);
}
