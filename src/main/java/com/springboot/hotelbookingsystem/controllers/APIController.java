package com.springboot.hotelbookingsystem.controllers;


import com.springboot.hotelbookingsystem.models.RoomReservation;
import com.springboot.hotelbookingsystem.services.ReservationService;
import com.springboot.hotelbookingsystem.utilities.DateUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/reservations")
public class APIController {
    private final ReservationService reservationService;

    @Autowired
    public APIController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<RoomReservation> getRoomReservations(@RequestParam(name="date", required = false)String dateString){
        Date date = DateUtilities.createDateFromDateString(dateString);
        return this.reservationService.getRoomReservationsForDate(date);
    }
}
