package com.springboot.hotelbookingsystem.models;

import java.io.Serializable;

public class SearchRoomModel implements Serializable {
    private String checkin;
    private String checkout;
    private Long roomid;
    private String roomType;


    public SearchRoomModel(String checkin, String checkout, Long roomid, String roomType) {
        this.checkin = checkin;
        this.checkout = checkout;
        this.roomid = roomid;
        this.roomType = roomType;
    }

    public SearchRoomModel() {
    }

    public SearchRoomModel(String checkin, String checkout) {
        this.checkin = checkin;
        this.checkout = checkout;
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

    public Long getRoomid() {
        return roomid;
    }

    public void setRoomid(Long roomid) {
        this.roomid = roomid;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
}
