package com.springboot.hotelbookingsystem.repositories;

import com.springboot.hotelbookingsystem.models.Guest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends CrudRepository<Guest, Long> {
}