package com.springboot.hotelbookingsystem.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "room")
public class Room implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 50)
    @NotNull(message = "unit can't be null")
    private String unit;

    @Column(length = 300)
    private String description;

    @Column
    @NotNull
    private double price;

    @OneToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private RoomType type = new RoomType();

    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private Set<Reservation> reservations = new HashSet<Reservation>();

    public Room(@NotNull(message = "unit can't be null") String unit, RoomType type, String description, @NotNull double price,
                Set<Reservation> reservations) {
        super();
        this.unit = unit;
        this.type = type;
        this.description = description;
        this.price = price;
        this.reservations = reservations;
    }

    public Room() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }
}
