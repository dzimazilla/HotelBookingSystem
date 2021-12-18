package com.springboot.hotelbookingsystem.models;

import java.util.ArrayList;
import java.util.List;

public class RoomModel {
    private Long id;
    private String unit;
    private String description;
    private double price;
    private String type;
    private List<ReservationModel> reservations = new ArrayList<ReservationModel>();

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

    public List<ReservationModel> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationModel> reservations) {
        this.reservations = reservations;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
