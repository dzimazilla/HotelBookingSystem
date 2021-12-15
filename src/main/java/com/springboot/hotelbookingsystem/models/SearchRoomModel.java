package com.springboot.hotelbookingsystem.models;

public class SearchRoomModel {
    private String checkin;
    private String checkout;
    private Long roomid;


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
}
