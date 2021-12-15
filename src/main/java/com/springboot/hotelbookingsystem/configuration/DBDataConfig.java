package com.springboot.hotelbookingsystem.configuration;

import com.springboot.hotelbookingsystem.entities.*;
import com.springboot.hotelbookingsystem.repositories.ReservationRepository;
import com.springboot.hotelbookingsystem.repositories.RoomRepository;
import com.springboot.hotelbookingsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.SimpleDateFormat;
import java.util.List;

@Configuration
public class DBDataConfig {

    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public DBDataConfig(BCryptPasswordEncoder bCryptPasswordEncoder) {
    
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    CommandLineRunner loadData(RoomRepository roomRepository, UserRepository userRepository, ReservationRepository reservationRepository) {
        String password = bCryptPasswordEncoder.encode("123456");
        return args -> {
            Room room1 = new Room("101", "good review", 180, null);
            Room room2 = new Room("102", "good price", 280, null);
            Room room3 = new Room("103", "comfortable", 160, null);
            Room room4 = new Room("104", "big bed", 200, null);
            Room room5 = new Room("105", "big window", 280, null);
            Room room6 = new Room("106", "just so so ", 120, null);
            roomRepository.saveAll(List.of(room1, room2, room3, room4, room5, room6));
            Role adminRole = new Role("admin");
            Role clientRole = new Role("client");
            Address address = new Address("1 main st", "Montreal", "QC", "M1M1M1", "Canada");
            User adminUser = new User("user", password, "user@gmail.com", "5141111111", address, adminRole);
            User clientUser = new User("lily", password, "lily@gmail.com", "5142222222", address, clientRole);
            userRepository.saveAll(List.of(adminUser, clientUser));
            Reservation reservation1 = new Reservation(
                    new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-13"),
                    new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-15"),
                    room1.getPrice(),
                    "", clientUser, room1);
            Reservation reservation2 = new Reservation(
                    new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-25"),
                    new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-29"),
                    room1.getPrice(),
                    "", clientUser, room1);
            Reservation reservation3 = new Reservation(
                    new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-01"),
                    new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-20"),
                    room1.getPrice(),
                    "", clientUser, room2);
            reservationRepository.saveAll(List.of(reservation1, reservation2, reservation3));
        };
    }
}

