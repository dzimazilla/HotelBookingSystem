package com.springboot.hotelbookingsystem.controllers;


import com.springboot.hotelbookingsystem.entities.Room;
import com.springboot.hotelbookingsystem.models.OrderModel;
import com.springboot.hotelbookingsystem.models.RegisterModel;
import com.springboot.hotelbookingsystem.models.SearchRoomModel;
import com.springboot.hotelbookingsystem.models.UserModel;
import com.springboot.hotelbookingsystem.security.CustomUserDetails;
import com.springboot.hotelbookingsystem.services.HotelBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@Controller
public class HotelBookingController {

    @Autowired
    private final HotelBookingService hotelBookingService;


    public HotelBookingController(HotelBookingService hotelBookingService) {
        super();
        this.hotelBookingService = hotelBookingService;
    }

    @GetMapping({"/home", "/"})
    public String showDefault(Model model) {
        return "home";
    }

    @GetMapping("/index")
    public String showIndexPage(final HttpServletRequest request, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
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
        return hotelBookingService.registerUser(register);
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return hotelBookingService.showLoginForm(model);
    }

    @GetMapping("/admin/user")
    public String listUsers(BindingResult result, Model model) {
        return hotelBookingService.listUsers(result, model);
    }


    @PostMapping("/login")
    public String login(@Valid UserModel user, BindingResult result, Model model) {
        return "redirect:/index";
    }

    @PostMapping("/searchRoom")
    public String searchRoom(@Valid SearchRoomModel searchRoom, BindingResult result, Model model) {
        Room room = hotelBookingService.searchRoom(searchRoom, result, model);
        if (room == null) {
            return "redirect:/index";
        }
        searchRoom.setRoomid(room.getId());
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
}
