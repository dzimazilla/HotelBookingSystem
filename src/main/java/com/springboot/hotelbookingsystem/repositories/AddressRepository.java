package com.example.hotelbookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hotelbookingsystem.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
