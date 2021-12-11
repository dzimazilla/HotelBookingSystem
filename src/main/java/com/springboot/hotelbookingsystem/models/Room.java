package com.springboot.hotelbookingsystem.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ROOM_ID", nullable = false)
    private Long roomId;

    @Length(min = 1, max = 16)
    @NotNull
    @Column(name = "NAME")
    private String roomName;

    @NotNull
    @Column(name="ROOM_NUMBER")
    private String roomNumber;

    @NotNull
    @Column(name="BED_INFO")
    private Integer bedInfo;

    @Column(name="TYPE")
    private String type;
}
