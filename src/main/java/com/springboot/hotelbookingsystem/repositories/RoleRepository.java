package com.springboot.hotelbookingsystem.repositories;


import com.springboot.hotelbookingsystem.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
