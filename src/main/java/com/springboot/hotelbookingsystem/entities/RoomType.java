package com.springboot.hotelbookingsystem.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "roomtype")
public class RoomType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100)
    private String name;
    @OneToOne(mappedBy = "type")
    private Room room;

    public RoomType(String name) {

        this.name = name;
    }

    public RoomType() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
