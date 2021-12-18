package com.springboot.hotelbookingsystem.models;

import java.io.Serializable;

public class ReservationModel implements Serializable {

    private Long id;
    private String checkin;
    private String checkout;
    private double total;
    private String remark;
    private String username;
    private String roomunit;

    public ReservationModel() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoomunit() {
        return roomunit;
    }

    public void setRoomunit(String roomunit) {
        this.roomunit = roomunit;
    }
}
