package com.springboot.hotelbookingsystem.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "address")
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100)
    private String address;
    @Column(length = 100)
    private String city;
    @Column(length = 100)
    private String province;
    @Column(length = 100)
    private String postcode;
    @Column(length = 100)
    private String country;
    @OneToOne(mappedBy = "address")
    private User user;

    public Address() {
    }

    public Address(String address, String city, String province, String postcode, String country) {
        this.address = address;
        this.city = city;
        this.province = province;
        this.postcode = postcode;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
