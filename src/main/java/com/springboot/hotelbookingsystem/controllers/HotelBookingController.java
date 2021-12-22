package com.springboot.hotelbookingsystem.controllers;


import com.springboot.hotelbookingsystem.entities.Room;
import com.springboot.hotelbookingsystem.models.*;
import com.springboot.hotelbookingsystem.security.CustomUserDetails;
import com.springboot.hotelbookingsystem.services.HotelBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@Controller
public class HotelBookingController {

    @Autowired
    private final HotelBookingService hotelBookingService;


    public HotelBookingController(HotelBookingService hotelBookingService) {
        super();
        this.hotelBookingService = hotelBookingService;
    }

    @GetMapping({"/home", "/"})
    public String showDefault(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        if (customUserDetails != null) {
            model.addAttribute("role", customUserDetails.getRoleName());
        }
        return "home";
    }

    @GetMapping("/index")
    public String showIndexPage(final HttpServletRequest request, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        if (customUserDetails != null) {
            model.addAttribute("role", customUserDetails.getRoleName());
        }
        if (request.getSession().getAttribute("MYSESSION") != null) {
            SearchRoomModel searchRoom = (SearchRoomModel) request.getSession().getAttribute("MYSESSION");
            request.getSession().invalidate();
            return hotelBookingService.showCheckoutPage(customUserDetails, searchRoom, null, model);
        }

        return hotelBookingService.showIndexPage(model);
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("register", new RegisterModel());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid RegisterModel register, BindingResult result, Model model) {
        return hotelBookingService.registerUser(register, result, model);
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return hotelBookingService.showLoginForm(model);
    }

    @GetMapping("/login-error")
    public String showLoginErrorForm(Model model) {
        return hotelBookingService.showLoginErrorForm(model);
    }

    @PostMapping("/login")
    public String login(@Valid UserModel user, BindingResult result, Model model) {
        return "redirect:/index";
    }

    @PostMapping("/searchRoom")
    public String searchRoom(@Valid SearchRoomModel searchRoom, BindingResult result, Model model) {
        try {
            Date checkin = new SimpleDateFormat("yyyy-MM-dd").parse(searchRoom.getCheckin());
            Date checkout = new SimpleDateFormat("yyyy-MM-dd").parse(searchRoom.getCheckout());
            long diffInMillies = Math.abs(checkout.getTime() - checkin.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            if (checkin.compareTo(new Date()) < 0) {
                FieldError pwErr = new FieldError("searchRoom", "checkin", "checkin date cannot be tody or before date");
                result.addError(pwErr);
            }
            if (diff < 1) {
                FieldError pwErr = new FieldError("searchRoom", "checkout", "checkout date cannot be same or before checkin date");
                result.addError(pwErr);
            }
            if (result.hasErrors()) {
                model.addAttribute("errors", result.getFieldErrors());
                model.addAttribute("searchRoom", new SearchRoomModel());
                model.addAttribute("roomTypes", hotelBookingService.getAllRoomType());
                return "index";
            }
        } catch (ParseException e1) {
        }

        Room room = hotelBookingService.searchRoom(searchRoom, result, model);
        if (room == null) {
            return "redirect:/index";
        }
        searchRoom.setRoomid(room.getId());
        searchRoom.setRoomType(room.getType().getName());
        model.addAttribute("searchRoom", searchRoom);
        return "reservation";
    }

    @PostMapping("/checkout")
    public String showCheckoutPage(final HttpServletRequest request, @AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid SearchRoomModel searchRoom, BindingResult result, Model model) {
        if (customUserDetails == null) {
            request.getSession().setAttribute("MYSESSION", searchRoom);
            return "redirect:/login";
        }

        return hotelBookingService.showCheckoutPage(customUserDetails, searchRoom, result, model);
    }

    @PostMapping("/placeorder")
    public String checkout(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid OrderModel order, BindingResult result, Model model) {
        order.setUserId(customUserDetails.getUser().getId());
        return hotelBookingService.placeorder(order, result, model);
    }

    @GetMapping("/admin/user")
    public String adminUser(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        return hotelBookingService.listUsers(model);
    }

    @GetMapping("/admin/user/add")
    public String adminAddUserPage(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        return hotelBookingService.addUserPage(model);
    }

    @PostMapping("/admin/user/add")
    public String adminAddUser(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid UserModel user, BindingResult result, Model model) {
        return hotelBookingService.addUser(user, result, model);
    }

    @GetMapping("/admin/user/view/{id}")
    public String adminViewUser(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model, @PathVariable("id") long id) {
        return hotelBookingService.viewUser(model, id);
    }

    @PostMapping("/admin/user/edit")
    public String adminUpdateUser(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid UserModel user, BindingResult result, Model model) {
        return hotelBookingService.updateUser(model, user, result);
    }

    @GetMapping("/admin/user/edit/{id}")
    public String adminEditUser(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model, @PathVariable("id") long id) {
        return hotelBookingService.editUser(model, id);
    }

    @GetMapping("/admin/room")
    public String adminRoom(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        return hotelBookingService.listRooms(model);
    }

    @GetMapping("/admin/room/add")
    public String adminAddRoomPage(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        return hotelBookingService.addRoomPage(model);
    }

    @PostMapping("/admin/room/add")
    public String adminAddRoom(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid RoomModel roomModel, BindingResult result, Model model) {
        return hotelBookingService.addRoom(roomModel, result, model);
    }

    @GetMapping("/admin/room/view/{id}")
    public String adminViewRoom(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model, @PathVariable("id") long id) {
        return hotelBookingService.viewRoom(model, id);
    }

    @PostMapping("/admin/room/edit")
    public String adminUpdateRoom(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid RoomModel room, BindingResult result, Model model) {
        return hotelBookingService.updateRoom(model, room, result);
    }

    @GetMapping("/admin/room/edit/{id}")
    public String adminEditRoom(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model, @PathVariable("id") long id) {
        return hotelBookingService.editRoom(model, id);
    }

    @GetMapping("/admin/reservation")
    public String adminReservation(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        return hotelBookingService.listReservations(model);
    }

    @GetMapping("/admin/reservation/add")
    public String adminAddReservationPage(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        return hotelBookingService.addReservationPage(model);
    }

    @PostMapping("/admin/reservation/add")
    public String adminAddReservation(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid ReservationModel reservationModel, BindingResult result, Model model) {
        return hotelBookingService.addReservation(reservationModel, result, model);
    }

    @GetMapping("/admin/reservation/view/{id}")
    public String adminViewReservation(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model, @PathVariable("id") long id) {
        return hotelBookingService.viewReservation(model, id);
    }

    @PostMapping("/admin/reservation/edit")
    public String adminUpdateReservation(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid ReservationModel reservation, BindingResult result, Model model) {
        return hotelBookingService.updateReservation(model, reservation, result);
    }

    @GetMapping("/admin/reservation/edit/{id}")
    public String adminEditReservation(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model, @PathVariable("id") long id) {
        return hotelBookingService.editReservation(model, id);
    }

    @GetMapping("/admin/reservation/delete/{id}")
    public String adminDeleteReservation(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model, @PathVariable("id") long id) {
        return hotelBookingService.deleteReservation(model, id);
    }
}
