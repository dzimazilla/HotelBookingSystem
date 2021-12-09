package com.springboot.hotelbookingsystem.repositories;

import com.springboot.hotelbookingsystem.models.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {
}
