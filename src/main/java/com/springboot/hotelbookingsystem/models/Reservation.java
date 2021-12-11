package com.springboot.hotelbookingsystem.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RESERVATION_ID", nullable = false)
    private Long reservationId;

    @Column(name = "ROOM_ID", nullable = false)
    private Long roomId;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "RESERVATION_DATE", nullable = false)
    private Date reservationDate;
}
