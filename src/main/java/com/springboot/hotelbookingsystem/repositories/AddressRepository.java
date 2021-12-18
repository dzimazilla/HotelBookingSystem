package com.springboot.hotelbookingsystem.repositories;


import com.springboot.hotelbookingsystem.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
