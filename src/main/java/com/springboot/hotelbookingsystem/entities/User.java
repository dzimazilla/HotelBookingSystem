package com.springboot.hotelbookingsystem.entities;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 50)
    @NotNull(message = "user name can't be null")
    private String username;

    @Column(length = 500)
    @NotNull(message = "password can't be null")
    private String password;

    @Column(length = 100)
    @NotNull(message = "email can't be null")
    @Email
    private String email;

    @Column(length = 100)
    @NotNull(message = "mobile can't be null")
    private String mobile;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address = new Address();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role = new Role();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private Set<Reservation> reservations = new HashSet<Reservation>();

    public User(@NotNull(message = "user name can't be null") String username,
                @NotNull(message = "password can't be null") String password,
                @NotNull(message = "email can't be null") @Email String email,
                @NotNull(message = "mobile can't be null") String mobile, Address address, Role role
    ) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.role = role;
    }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }
}
