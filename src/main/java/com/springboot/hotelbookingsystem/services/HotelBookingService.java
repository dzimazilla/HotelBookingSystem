package com.example.hotelbookingsystem.service;

import com.example.hotelbookingsystem.entity.Address;
import com.example.hotelbookingsystem.entity.Reservation;
import com.example.hotelbookingsystem.entity.Role;
import com.example.hotelbookingsystem.entity.Room;
import com.example.hotelbookingsystem.entity.User;
import com.example.hotelbookingsystem.model.UserModel;
import com.example.hotelbookingsystem.model.OrderModel;
import com.example.hotelbookingsystem.model.RegisterModel;
import com.example.hotelbookingsystem.model.SearchRoomModel;
import com.example.hotelbookingsystem.repository.AddressRepository;
import com.example.hotelbookingsystem.repository.ReservationRepository;
import com.example.hotelbookingsystem.repository.RoleRepository;
import com.example.hotelbookingsystem.repository.RoomRepository;
import com.example.hotelbookingsystem.repository.UserRepository;
import com.example.hotelbookingsystem.security.CustomUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

@Service
public class HotelBookingService {

    @Autowired
    private final AddressRepository addressRepository;
    @Autowired
    private final ReservationRepository reservationRepository;
    @Autowired
    private final RoleRepository roleRepository;
    @Autowired
    private final RoomRepository roomRepository;
    @Autowired
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public HotelBookingService(AddressRepository addressRepository, ReservationRepository reservationRepository,
			RoleRepository roleRepository, RoomRepository roomRepository, UserRepository userRepository,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		super();
		this.addressRepository = addressRepository;
		this.reservationRepository = reservationRepository;
		this.roleRepository = roleRepository;
		this.roomRepository = roomRepository;
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

    @Transactional
    public String register(BindingResult result) {
//    public String register(@Valid BlogUser blogUser, BindingResult result) {
//        if (!blogUser.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,100}$")) {
//            FieldError pwErr = new FieldError("blogUser", "password", PW_ERROR);
//            result.addError(pwErr);
//        }
//        boolean exists = blogUserRepository.findByUsername(blogUser.getUsername()).isPresent();
//        if (exists) {
//            FieldError nameTaken = new FieldError("blogUser", "username", "Username is taken");
//            result.addError(nameTaken);
//        }
//        if (!blogUser.getPassword().equals(blogUser.getPasswordRepeat())) {
//            FieldError pwRepeat = new FieldError("blogUser", "passwordRepeat", "Please enter the same password");
//            result.addError(pwRepeat);
//        }
//        System.out.println(result);
//        if (result.hasErrors()) {
//            return "register";
//        }
//        String encodePW = bCryptPasswordEncoder.encode(blogUser.getPassword());
//        System.out.println(encodePW);
//        blogUser.setPassword(encodePW);
//        blogUserRepository.save(blogUser);
        return "register";
    }
    
    @Transactional
    public String listUsers(BindingResult result, Model model) {
    	List<User> users = userRepository.findAll();
    	model.addAttribute("users",users);
    	return "adminusers";
    	
    }
    
    @Transactional
    public String showCheckoutPage(CustomUserDetails customUserDetails, SearchRoomModel searchRoom,BindingResult result, Model model) {
    	//List<User> users = userRepository.findAll();
    	OrderModel orderModel = new OrderModel();
    	orderModel.setCheckin(searchRoom.getCheckin());
    	orderModel.setCheckout(searchRoom.getCheckout());
    	orderModel.setRoomId(searchRoom.getRoomid());
    	Room room = roomRepository.findById(searchRoom.getRoomid())
		          .orElseThrow(() -> new IllegalArgumentException("Invalid room Id:" + searchRoom.getRoomid()));
	    long diffInMillies=0;
		try {
			diffInMillies = Math.abs(new SimpleDateFormat("yyyy-MM-dd").parse(searchRoom.getCheckout()).getTime() - new SimpleDateFormat("yyyy-MM-dd").parse(searchRoom.getCheckin()).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
	    orderModel.setTotalPrice(diff*room.getPrice());
	    model.addAttribute("order",orderModel);
    	model.addAttribute("room",room);
    	return "checkout";
    }

    @Transactional
    public String placeorder(OrderModel order,BindingResult result, Model model) {
    	//List<User> users = userRepository.findAll();
    	OrderModel orderModel = new OrderModel();
    	orderModel.setCheckin(order.getCheckin());
    	orderModel.setCheckout(order.getCheckout());
    	User user = userRepository.findById(order.getUserId())
		          .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + order.getUserId()));
    	Room room = roomRepository.findById(order.getRoomId())
		          .orElseThrow(() -> new IllegalArgumentException("Invalid room Id:" + order.getRoomId()));
    	Reservation reservation = new Reservation();
    	try {
			reservation.setCheckin(new SimpleDateFormat("yyyy-MM-dd").parse(order.getCheckin()));
			reservation.setCheckout(new SimpleDateFormat("yyyy-MM-dd").parse(order.getCheckout()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	reservation.setRoom(room);
    	reservation.setUser(user);
    	reservation.setTotal(order.getTotalPrice());
    	reservationRepository.save(reservation);
    	model.addAttribute("order",orderModel);
    	return "confirm";
    }
    
    @Transactional
    public String showLoginForm(Model model) {
    	model.addAttribute("user",new UserModel());
    	return "login";
    }
    @Transactional
    public String registerUser(RegisterModel register) {
    	User user = new User();
    	Address address = new Address();
    	address.setAddress(register.getAddress());
    	address.setCity(register.getCity());
    	address.setCountry(register.getCountry());
    	address.setPostcode(register.getPostcode());
    	address.setProvince(register.getProvince());
    	user.setUsername(register.getUsername());
    	user.setAddress(address);
    	user.setEmail(register.getEmail());
    	user.setMobile(register.getMobile());
    	user.setPassword(bCryptPasswordEncoder.encode(register.getPassword()));
    	Role role = roleRepository.findById(Long.valueOf("2"))
		          .orElseThrow(() -> new IllegalArgumentException("Invalid role  Id: 2"));
    	user.setRole(role);
    	userRepository.save(user);
    	return "redirect:/login";
    }
    
    @Transactional
    public String showIndexPage(Model model) {
    	model.addAttribute("searchRoom",new SearchRoomModel());
    	return "index";
    }
    
    @Transactional
    public Room searchRoom(@Valid SearchRoomModel searchRoom, BindingResult result, Model model) {
    	List<Room> rooms = roomRepository.findAll();
    	for(Room room:rooms) {
    		for(Reservation reservation:room.getReservations()) {
    	    	List<Date> checkinList = new ArrayList<Date>();
    	    	List<Date> checkoutList = new ArrayList<Date>();
    			checkinList.add(reservation.getCheckin());
    			checkoutList.add(reservation.getCheckout());
    	    	Collections.sort(checkinList, new SortByDate());
    	    	Collections.sort(checkoutList, new SortByDate());
    	    	try {
					if(checkAvailable(searchRoom.getCheckin(), searchRoom.getCheckout(), checkinList,checkoutList)) {
						return room;
					}
				} catch (ParseException e) {

					model.addAttribute("errors","Internal Error");
					return null;
				}
    		}
    	}
    	model.addAttribute("errors","Room not available");
    	return null;
    }
    
    private boolean checkAvailable(String checkin, String checkout, List<Date> checkinList, List<Date> checkoutList) throws ParseException {
    	if(new SimpleDateFormat("yyyy-MM-dd").parse(checkout).compareTo(checkinList.get(0))<=0) {
    		return true;
    	}
    	if(new SimpleDateFormat("yyyy-MM-dd").parse(checkin).compareTo(checkoutList.get(checkoutList.size()-1))>=0) {
    		return true;
    	}
    	for(int i=0; i<checkinList.size();i++) {
    		if(new SimpleDateFormat("yyyy-MM-dd").parse(checkin).compareTo(checkinList.get(i))>0
    				&&new SimpleDateFormat("yyyy-MM-dd").parse(checkin).compareTo(checkoutList.get(i))<0) {
    			return false;
    		}
    		if(new SimpleDateFormat("yyyy-MM-dd").parse(checkout).compareTo(checkinList.get(i))>0
    				&&new SimpleDateFormat("yyyy-MM-dd").parse(checkout).compareTo(checkoutList.get(i))<0) {
    			return false;
    		}
    	}
    	boolean result = false;
    	for(int i=0; i<checkinList.size();i++) {
    		if(new SimpleDateFormat("yyyy-MM-dd").parse(checkin).compareTo(checkoutList.get(i))>=0
    				&&new SimpleDateFormat("yyyy-MM-dd").parse(checkin).compareTo(checkinList.get(i+1))<=0
    				&&new SimpleDateFormat("yyyy-MM-dd").parse(checkout).compareTo(checkoutList.get(i))>=0
    	    		&&new SimpleDateFormat("yyyy-MM-dd").parse(checkout).compareTo(checkinList.get(i+1))<=0){
    			return true;
    		}
    	}
    	return result;
    }
    static class SortByDate implements Comparator<Date> {
        @Override
        public int compare(Date a, Date b) {
            return a.compareTo(b);
        }
    }
}
