package com.springboot.hotelbookingsystem.services;

import com.springboot.hotelbookingsystem.domain.RoomReservation;
import com.springboot.hotelbookingsystem.models.Guest;
import com.springboot.hotelbookingsystem.models.Reservation;
import com.springboot.hotelbookingsystem.models.Room;
import com.springboot.hotelbookingsystem.repositories.GuestRepository;
import com.springboot.hotelbookingsystem.repositories.ReservationRepository;
import com.springboot.hotelbookingsystem.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReservationService {
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(RoomRepository roomRepository, GuestRepository guestRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<RoomReservation> getRoomReservationsForDate(Date date) {
        Iterable<Room> rooms = this.roomRepository.findAll();
        Map<Long,RoomReservation> roomReservationMap = new HashMap<>();
        rooms.forEach(room ->{
            RoomReservation roomReservation = new RoomReservation();
            roomReservation.setRoomId(room.getRoomId());
            roomReservation.setRoomName(room.getRoomName());
            roomReservation.setRoomNumber(room.getRoomNumber());
            roomReservationMap.put(room.getRoomId(), roomReservation);
        } );
        Iterable<Reservation>reservations=this.reservationRepository.findReservationByReservationDate(new java.sql.Date(date.getTime()));
        reservations.forEach(reservation -> {
            RoomReservation roomReservation = roomReservationMap.get(reservation.getRoomId());
            roomReservation.setDate(date);
            Guest guest = this.guestRepository.findById(reservation.getGuestId()).get();
            roomReservation.setFirstName(guest.getFirstName());
            roomReservation.setLastName(guest.getLastName());
            roomReservation.setGuestId(guest.getGuestId());
        });
        List<RoomReservation>roomReservations = new ArrayList<>();
        for (Long id: roomReservationMap.keySet()){
            roomReservations.add(roomReservationMap.get(id));

        }
        return roomReservations;
    }
}
