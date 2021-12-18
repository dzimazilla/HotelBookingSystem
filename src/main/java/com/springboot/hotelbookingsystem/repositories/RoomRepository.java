package com.springboot.hotelbookingsystem.repositories;

import com.springboot.hotelbookingsystem.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT r FROM Room r WHERE r.type.name = ?1")
    List<Room> findAllByRoomType(String roomType);

    @Query("SELECT r FROM Room r WHERE r.unit = ?1")
    Room findRoomByUnit(String unit);
}
