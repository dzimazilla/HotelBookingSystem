package com.springboot.hotelbookingsystem.repositories;

import com.springboot.hotelbookingsystem.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}