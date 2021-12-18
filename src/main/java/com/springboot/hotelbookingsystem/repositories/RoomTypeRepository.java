package com.springboot.hotelbookingsystem.repositories;


import com.springboot.hotelbookingsystem.entities.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {

    @Query("SELECT rt FROM RoomType rt WHERE rt.name = ?1")
    RoomType findRoomTypeByname(String name);
}
