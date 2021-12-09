package com.springboot.hotelbookingsystem.controllers;

import com.springboot.hotelbookingsystem.domain.RoomReservation;
import com.springboot.hotelbookingsystem.services.ReservationService;
import com.springboot.hotelbookingsystem.utilities.DateUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/reservations")
public class RoomReservationController {
    private final ReservationService reservationService;

    @Autowired
    public RoomReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public String getReservations (@RequestParam(value = "date", required = false)String dateString, Model model) {
        Date date = DateUtilities.createDateFromDateString(dateString);
        List<RoomReservation> roomReservations = this.reservationService.getRoomReservationsForDate(date);
        model.addAttribute("roomReservations", roomReservations);
        return "reservations";
    }
}
