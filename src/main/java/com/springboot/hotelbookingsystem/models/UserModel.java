package com.springboot.hotelbookingsystem.models;

import java.util.ArrayList;
import java.util.List;

public class UserModel {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String mobile;
    private String address;
    private String city;
    private String province;
    private String postcode;
    private String country;
    private List<ReservationModel> reservations = new ArrayList<ReservationModel>();

    public UserModel(String username, String password, String email, String mobile, String address, String city,
                     String province, String postcode, String country) {
        super();
        this.username = username;
        this.password = password;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.city = city;
        this.province = province;
        this.postcode = postcode;
        this.country = country;
    }

    public UserModel() {

    }

    public UserModel(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<ReservationModel> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationModel> reservations) {
        this.reservations = reservations;
    }

}
