package com.springboot.hotelbookingsystem.services;

import com.springboot.hotelbookingsystem.entities.*;
import com.springboot.hotelbookingsystem.models.*;
import com.springboot.hotelbookingsystem.repositories.*;
import com.springboot.hotelbookingsystem.security.CustomUserDetails;
import com.springboot.hotelbookingsystem.utilities.BookingUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private final RoomTypeRepository roomTypeRepository;
    //@Autowired
    //private JavaMailSender mailSender;


    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public HotelBookingService(AddressRepository addressRepository, ReservationRepository reservationRepository,
                               RoleRepository roleRepository, RoomRepository roomRepository, UserRepository userRepository,
                               BCryptPasswordEncoder bCryptPasswordEncoder, RoomTypeRepository roomTypeRepository) {
        this.addressRepository = addressRepository;
        this.reservationRepository = reservationRepository;
        this.roleRepository = roleRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roomTypeRepository = roomTypeRepository;
    }

    @Transactional
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("role", "admin");
        return "adminuser";

    }

    @Transactional
    public String addUserPage(Model model) {
        model.addAttribute("user", new UserModel());
        model.addAttribute("role", "admin");
        return "adminadduser";

    }

    @Transactional
    public String addUser(@Valid UserModel userModel, BindingResult result, Model model) {
        User existedUser = userRepository.findByUserName(userModel.getUsername());
        if (existedUser != null) {
            FieldError usernameErr = new FieldError("user", "username", "user name existed");
            result.addError(usernameErr);
        }
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getFieldErrors());
            model.addAttribute("user", userModel);
            return "adminadduser";
        }
        User user = new User();
        Address address = new Address();
        address.setAddress(userModel.getAddress());
        address.setCity(userModel.getCity());
        address.setCountry(userModel.getCountry());
        address.setPostcode(userModel.getPostcode());
        address.setProvince(userModel.getProvince());
        user.setUsername(userModel.getUsername());
        user.setAddress(address);
        user.setEmail(userModel.getEmail());
        user.setMobile(userModel.getMobile());
        user.setPassword(bCryptPasswordEncoder.encode(userModel.getPassword()));
        Role role = roleRepository.findById(Long.valueOf(2))
                .orElseThrow(() -> new IllegalArgumentException("Invalid role  Id: 2"));
        user.setRole(role);
        userRepository.save(user);
        return "redirect:/admin/user";
    }

    @Transactional
    public String viewUser(Model model, long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        UserModel userModel = new UserModel();
        userModel.setId(id);
        userModel.setUsername(user.getUsername());
        userModel.setEmail(user.getEmail());
        userModel.setMobile(user.getMobile());
        userModel.setAddress(user.getAddress().getAddress());
        userModel.setCity(user.getAddress().getCity());
        userModel.setProvince(user.getAddress().getProvince());
        userModel.setPostcode(user.getAddress().getPostcode());
        userModel.setCountry(user.getAddress().getCountry());
        List<Reservation> reservations = reservationRepository.findByUserId(id);
        List<ReservationModel> reservationModels = new ArrayList<ReservationModel>();
        for (Reservation reservation : reservations) {
            ReservationModel reservationModel = new ReservationModel();
            reservationModel.setCheckin(BookingUtils.convertDateString(reservation.getCheckin(), null));
            reservationModel.setCheckout(BookingUtils.convertDateString(reservation.getCheckout(), null));
            reservationModel.setRoomunit(reservation.getRoom().getUnit());
            reservationModel.setRemark(reservation.getRemark());
            reservationModel.setTotal(reservation.getTotal());
            reservationModel.setId(reservation.getId());
            reservationModels.add(reservationModel);
        }
        userModel.setReservations(reservationModels);
        model.addAttribute("user", userModel);
        model.addAttribute("role", "admin");
        return "adminviewuser";
    }

    @Transactional
    public String updateUser(Model model, @Valid UserModel userModel, BindingResult result) {
        User user = userRepository.findById(userModel.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userModel.getId()));
        user.setEmail(userModel.getEmail());
        user.setMobile(userModel.getMobile());
        user.getAddress().setAddress(userModel.getAddress());
        user.getAddress().setCity(userModel.getCity());
        user.getAddress().setProvince(userModel.getProvince());
        user.getAddress().setPostcode(userModel.getPostcode());
        user.getAddress().setCountry(userModel.getCountry());
        userRepository.save(user);
        return "redirect:/admin/user";
    }

    @Transactional
    public String editUser(Model model, long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        UserModel userModel = new UserModel();
        userModel.setId(id);
        userModel.setUsername(user.getUsername());
        userModel.setEmail(user.getEmail());
        userModel.setMobile(user.getMobile());
        userModel.setAddress(user.getAddress().getAddress());
        userModel.setCity(user.getAddress().getCity());
        userModel.setProvince(user.getAddress().getProvince());
        userModel.setPostcode(user.getAddress().getPostcode());
        userModel.setCountry(user.getAddress().getCountry());
        List<Reservation> reservations = reservationRepository.findByUserId(id);
        List<ReservationModel> reservationModels = new ArrayList<ReservationModel>();
        for (Reservation reservation : reservations) {
            ReservationModel reservationModel = new ReservationModel();
            reservationModel.setCheckin(BookingUtils.convertDateString(reservation.getCheckin(), null));
            reservationModel.setCheckout(BookingUtils.convertDateString(reservation.getCheckout(), null));
            reservationModel.setRoomunit(reservation.getRoom().getUnit());
            reservationModel.setRemark(reservation.getRemark());
            reservationModel.setTotal(reservation.getTotal());
            reservationModel.setId(reservation.getId());
            reservationModels.add(reservationModel);
        }
        userModel.setReservations(reservationModels);
        model.addAttribute("user", userModel);
        model.addAttribute("role", "admin");
        return "adminedituser";

    }

    @Transactional
    public String listRooms(Model model) {
        List<Room> rooms = roomRepository.findAll();
        model.addAttribute("rooms", rooms);
        model.addAttribute("role", "admin");
        return "adminroom";
    }

    @Transactional
    public String addRoomPage(Model model) {
        List<RoomType> roomTypes = roomTypeRepository.findAll();
        model.addAttribute("roomTypes", roomTypes);
        model.addAttribute("room", new RoomModel());
        model.addAttribute("role", "admin");
        return "adminaddroom";

    }

    @Transactional
    public String addRoom(@Valid RoomModel roomModel, BindingResult result, Model model) {
        Room existedRoom = roomRepository.findRoomByUnit(roomModel.getUnit());
        if (existedRoom != null) {
            FieldError usernameErr = new FieldError("room", "unit", "unit existed");
            result.addError(usernameErr);
        }
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getFieldErrors());
            model.addAttribute("room", roomModel);
            List<RoomType> roomTypes = roomTypeRepository.findAll();
            model.addAttribute("roomTypes", roomTypes);
            model.addAttribute("role", "admin");
            return "adminaddroom";
        }
        RoomType roomType = roomTypeRepository.findRoomTypeByname(roomModel.getType());
        Room room = new Room();
        room.setDescription(roomModel.getDescription());
        room.setPrice(roomModel.getPrice());
        room.setType(roomType);
        room.setUnit(roomModel.getUnit());
        roomRepository.save(room);
        return "redirect:/admin/room";
    }

    @Transactional
    public String viewRoom(Model model, long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Room Id:" + id));
        RoomModel roomModel = new RoomModel();
        roomModel.setId(id);
        roomModel.setUnit(room.getUnit());
        roomModel.setDescription(room.getDescription());
        roomModel.setPrice(room.getPrice());
        roomModel.setType(room.getType().getName());
        List<Reservation> reservations = reservationRepository.findByRoomId(id);
        List<ReservationModel> reservationModels = new ArrayList<ReservationModel>();
        for (Reservation reservation : reservations) {
            ReservationModel reservationModel = new ReservationModel();
            reservationModel.setCheckin(BookingUtils.convertDateString(reservation.getCheckin(), null));
            reservationModel.setCheckout(BookingUtils.convertDateString(reservation.getCheckout(), null));
            reservationModel.setRoomunit(reservation.getRoom().getUnit());
            reservationModel.setRemark(reservation.getRemark());
            reservationModel.setTotal(reservation.getTotal());
            reservationModel.setId(reservation.getId());
            reservationModels.add(reservationModel);
        }
        roomModel.setReservations(reservationModels);
        model.addAttribute("room", roomModel);
        model.addAttribute("role", "admin");
        return "adminviewroom";
    }

    @Transactional
    public String updateRoom(Model model, @Valid RoomModel roomModel, BindingResult result) {
        Room room = roomRepository.findById(roomModel.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid room Id:" + roomModel.getId()));
        room.setDescription(roomModel.getDescription());
        room.setPrice(roomModel.getPrice());
        RoomType roomType = roomTypeRepository.findRoomTypeByname(roomModel.getType());
        room.setType(roomType);
        roomRepository.save(room);
        return "redirect:/admin/room";
    }

    @Transactional
    public String editRoom(Model model, long id) {
        List<RoomType> roomTypes = roomTypeRepository.findAll();
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Room Id:" + id));
        RoomModel roomModel = new RoomModel();
        roomModel.setId(id);
        roomModel.setUnit(room.getUnit());
        roomModel.setDescription(room.getDescription());
        roomModel.setPrice(room.getPrice());
        roomModel.setType(room.getType().getName());
        List<Reservation> reservations = reservationRepository.findByRoomId(id);
        List<ReservationModel> reservationModels = new ArrayList<ReservationModel>();
        for (Reservation reservation : reservations) {
            ReservationModel reservationModel = new ReservationModel();
            reservationModel.setCheckin(BookingUtils.convertDateString(reservation.getCheckin(), null));
            reservationModel.setCheckout(BookingUtils.convertDateString(reservation.getCheckout(), null));
            reservationModel.setRoomunit(reservation.getRoom().getUnit());
            reservationModel.setRemark(reservation.getRemark());
            reservationModel.setTotal(reservation.getTotal());
            reservationModel.setId(reservation.getId());
            reservationModels.add(reservationModel);
        }
        roomModel.setReservations(reservationModels);
        model.addAttribute("room", roomModel);
        model.addAttribute("roomTypes", roomTypes);
        model.addAttribute("role", "admin");
        return "admineditroom";
    }

    @Transactional
    public String listReservations(Model model) {
        List<Reservation> reservations = reservationRepository.findAll();
        List<ReservationModel> reservationModels = new ArrayList<ReservationModel>();
        for (Reservation reservation : reservations) {
            ReservationModel reservationModel = new ReservationModel();
            reservationModel.setCheckin(BookingUtils.convertDateString(reservation.getCheckin(), null));
            reservationModel.setCheckout(BookingUtils.convertDateString(reservation.getCheckout(), null));
            reservationModel.setRoomunit(reservation.getRoom().getUnit());
            reservationModel.setRemark(reservation.getRemark());
            reservationModel.setTotal(reservation.getTotal());
            reservationModel.setId(reservation.getId());
            reservationModels.add(reservationModel);
        }
        model.addAttribute("reservations", reservationModels);
        model.addAttribute("role", "admin");
        return "adminreservation";
    }

    @Transactional
    public String addReservationPage(Model model) {
        List<Room> rooms = roomRepository.findAll();
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("rooms", rooms);
        model.addAttribute("reservation", new ReservationModel());
        model.addAttribute("role", "admin");
        return "adminaddreservation";

    }

    @Transactional
    public String addReservation(@Valid ReservationModel reservationModel, BindingResult result, Model model) {
        Room room = roomRepository.findRoomByUnit(reservationModel.getRoomunit());
        User user = userRepository.findByUserName(reservationModel.getUsername());
        Reservation reservation = new Reservation();
        reservation.setCheckin(BookingUtils.convertStringDate(reservationModel.getCheckin(), null));
        reservation.setCheckout(BookingUtils.convertStringDate(reservationModel.getCheckout(), null));
        reservation.setRemark(reservationModel.getRemark());
        reservation.setRoom(room);
        reservation.setUser(user);
        reservation.setTotal(room.getPrice() * BookingUtils.calculateDaysDiff(reservationModel.getCheckin(), reservationModel.getCheckout()));
        reservationRepository.save(reservation);
        return "redirect:/admin/reservation";
    }

    @Transactional
    public String viewReservation(Model model, long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Reservation Id:" + id));
        ReservationModel reservationModel = new ReservationModel();
        reservationModel.setId(id);
        reservationModel.setCheckin(BookingUtils.convertDateString(reservation.getCheckin(), null));
        reservationModel.setCheckout(BookingUtils.convertDateString(reservation.getCheckout(), null));
        reservationModel.setRemark(reservation.getRemark());
        reservationModel.setTotal(reservation.getTotal());
        reservationModel.setRoomunit(reservation.getRoom().getUnit());
        reservationModel.setUsername(reservation.getUser().getUsername());
        model.addAttribute("reservation", reservationModel);
        model.addAttribute("role", "admin");
        return "adminviewreservation";
    }

    @Transactional
    public String updateReservation(Model model, @Valid ReservationModel reservationModel, BindingResult result) {
        Reservation reservation = reservationRepository.findById(reservationModel.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Reservation Id:" + reservationModel.getId()));
        reservation.setCheckin(BookingUtils.convertStringDate(reservationModel.getCheckin(), null));
        reservation.setCheckout(BookingUtils.convertStringDate(reservationModel.getCheckout(), null));
        reservation.setRemark(reservationModel.getRemark());
        reservation.setTotal(reservation.getRoom().getPrice() * BookingUtils.calculateDaysDiff(reservationModel.getCheckin(), reservationModel.getCheckout()));
        reservationRepository.save(reservation);
        return "redirect:/admin/reservation";
    }

    @Transactional
    public String editReservation(Model model, long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Reservation Id:" + id));
        ReservationModel reservationModel = new ReservationModel();
        reservationModel.setId(id);
        reservationModel.setCheckin(BookingUtils.convertDateString(reservation.getCheckin(), null));
        reservationModel.setCheckout(BookingUtils.convertDateString(reservation.getCheckout(), null));
        reservationModel.setRemark(reservation.getRemark());
        reservationModel.setTotal(reservation.getTotal());
        reservationModel.setRoomunit(reservation.getRoom().getUnit());
        reservationModel.setUsername(reservation.getUser().getUsername());
        model.addAttribute("reservation", reservationModel);
        model.addAttribute("role", "admin");
        return "admineditreservation";
    }

    @Transactional
    public String deleteReservation(Model model, long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Reservation Id:" + id));
        Room room = reservation.getRoom();
        room.getReservations().remove(reservation);
        User user = userRepository.findById(reservation.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Reservation Id:" + reservation.getUser().getId()));
        user.getReservations().remove(reservation);
        roomRepository.save(room);
        userRepository.save(user);
        reservationRepository.delete(reservation);
        return "redirect:/admin/reservation";
    }

    @Transactional
    public String showCheckoutPage(CustomUserDetails customUserDetails, SearchRoomModel searchRoom, BindingResult result, Model model) {
        //List<User> users = userRepository.findAll();
        OrderModel orderModel = new OrderModel();
        orderModel.setCheckin(searchRoom.getCheckin());
        orderModel.setCheckout(searchRoom.getCheckout());
        orderModel.setRoomId(searchRoom.getRoomid());
        Room room = roomRepository.findById(searchRoom.getRoomid())
                .orElseThrow(() -> new IllegalArgumentException("Invalid room Id:" + searchRoom.getRoomid()));
        orderModel.setRoomType(room.getType().getName());
        long diffInMillies = 0;
        try {
            diffInMillies = Math.abs(new SimpleDateFormat("yyyy-MM-dd").parse(searchRoom.getCheckout()).getTime() - new SimpleDateFormat("yyyy-MM-dd").parse(searchRoom.getCheckin()).getTime());
        } catch (ParseException e) {

            e.printStackTrace();
        }
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        orderModel.setTotalPrice(diff * room.getPrice());
        User user = userRepository.findById(customUserDetails.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + customUserDetails.getUser().getId()));
        orderModel.setAddress(user.getAddress().getAddress());
        orderModel.setCity(user.getAddress().getCity());
        orderModel.setProvince(user.getAddress().getProvince());
        orderModel.setPostcode(user.getAddress().getPostcode());
        orderModel.setCountry(user.getAddress().getCountry());
        model.addAttribute("order", orderModel);
        model.addAttribute("room", room);
        return "checkout";
    }

    @Transactional
    public String placeorder(OrderModel order, BindingResult result, Model model) {
        User user = userRepository.findById(order.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + order.getUserId()));
        Room room = roomRepository.findById(order.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid room Id:" + order.getRoomId()));
        Reservation reservation = new Reservation();
        try {
            reservation.setCheckin(new SimpleDateFormat("yyyy-MM-dd").parse(order.getCheckin()));
            reservation.setCheckout(new SimpleDateFormat("yyyy-MM-dd").parse(order.getCheckout()));
        } catch (ParseException e) {

            e.printStackTrace();
        }
        order.setRoomunit(room.getUnit());
        order.setRoomdescription(room.getDescription());
        order.setAddress(user.getAddress().getAddress());
        order.setCity(user.getAddress().getCity());
        order.setProvince(user.getAddress().getProvince());
        order.setPostcode(user.getAddress().getPostcode());
        order.setCountry(user.getAddress().getCountry());
        reservation.setRoom(room);
        reservation.setUser(user);
        reservation.setTotal(order.getTotalPrice());
        reservationRepository.save(reservation);
        model.addAttribute("order", order);
        String subject = "Booking Confirmation";
        String body = buildEmailBody(order, room);
        sendMail(user.getEmail(), subject, body);
        return "confirm";
    }

    private String buildEmailBody(OrderModel order, Room room) {
        StringBuilder sb = new StringBuilder();
        sb.append("Congratulation, you have booked room successfully\n\n");
        sb.append("Room Unit:").append(order.getRoomunit()).append("\n");
        sb.append("Room type:").append(room.getType().getName()).append("\n");
        sb.append("Check in:").append(order.getCheckin()).append("\n");
        sb.append("Check out:").append(order.getCheckout()).append("\n");
        sb.append("------------------------");
        sb.append("Sunny Hotel");
        return sb.toString();
    }

    @Transactional
    public List<RoomType> getAllRoomType() {
        List<RoomType> roomTypes = roomTypeRepository.findAll();
        return roomTypes;
    }

    @Transactional
    public String showLoginForm(Model model) {
        model.addAttribute("user", new UserModel());
        return "login";
    }

    @Transactional
    public String showLoginErrorForm(Model model) {
        model.addAttribute("error", "username or password is wrong, please try again");
        model.addAttribute("user", new UserModel());
        return "login-error";
    }

    @Transactional
    public String registerUser(@Valid RegisterModel register, BindingResult result, Model model) {
        User existedUser = userRepository.findByUserName(register.getUsername());
        if (existedUser != null) {
            FieldError usernameErr = new FieldError("register", "username", "user name existed");
            result.addError(usernameErr);
        }
        if (!register.getPassword().equalsIgnoreCase(register.getRepeatpassword())) {
            FieldError pwErr = new FieldError("register", "password", "password doesn't match");
            result.addError(pwErr);
        }
        if (!org.apache.commons.lang3.StringUtils.isAlphanumeric(register.getPassword())
                || register.getPassword() != null && register.getPassword().length() < 6
                || register.getPassword() != null && register.getPassword().length() > 10) {
            FieldError pwErr = new FieldError("register", "password", "password must be number and letter,lenght is 6 to 10");
            result.addError(pwErr);
        }
        if (StringUtils.isBlank(register.getAddress())) {
            FieldError pwErr = new FieldError("register", "address", "address cannot be null");
            result.addError(pwErr);
        }
        if (StringUtils.isBlank(register.getCity())) {
            FieldError pwErr = new FieldError("register", "city", "city cannot be null");
            result.addError(pwErr);
        }
        if (StringUtils.isBlank(register.getPostcode())) {
            FieldError pwErr = new FieldError("register", "Postcode", "Postcode cannot be null");
            result.addError(pwErr);
        }
        if (StringUtils.isBlank(register.getProvince())) {
            FieldError pwErr = new FieldError("register", "Province", "Province cannot be null");
            result.addError(pwErr);
        }
        if (StringUtils.isBlank(register.getCountry())) {
            FieldError pwErr = new FieldError("register", "Country", "Country cannot be null");
            result.addError(pwErr);
        }
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getFieldErrors());
            model.addAttribute("register", register);
            return "register";
        }

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
        List<RoomType> roomTypes = roomTypeRepository.findAll();
        model.addAttribute("searchRoom", new SearchRoomModel());
        model.addAttribute("roomTypes", roomTypes);
        return "index";
    }


    @Transactional
    public Room searchRoom(@Valid SearchRoomModel searchRoom, BindingResult result, Model model) {
        List<Room> rooms = roomRepository.findAllByRoomType(searchRoom.getRoomType());
        for (Room room : rooms) {
            if (room.getReservations().size() == 0) {
                return room;
            }
            for (Reservation reservation : room.getReservations()) {
                List<Date> checkinList = new ArrayList<Date>();
                List<Date> checkoutList = new ArrayList<Date>();
                checkinList.add(reservation.getCheckin());
                checkoutList.add(reservation.getCheckout());
                Collections.sort(checkinList, new SortByDate());
                Collections.sort(checkoutList, new SortByDate());
                try {
                    if (checkAvailable(searchRoom.getCheckin(), searchRoom.getCheckout(), checkinList, checkoutList)) {
                        return room;
                    }
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    model.addAttribute("errors", "Internal Error");
                    return null;
                }
            }
        }
        model.addAttribute("errors", "Room not available");
        return null;
    }

    private boolean checkAvailable(String checkin, String checkout, List<Date> checkinList, List<Date> checkoutList) throws ParseException {
        if (new SimpleDateFormat("yyyy-MM-dd").parse(checkout).compareTo(checkinList.get(0)) <= 0) {
            return true;
        }
        if (new SimpleDateFormat("yyyy-MM-dd").parse(checkin).compareTo(checkoutList.get(checkoutList.size() - 1)) >= 0) {
            return true;
        }
        for (int i = 0; i < checkinList.size(); i++) {
            if (new SimpleDateFormat("yyyy-MM-dd").parse(checkin).compareTo(checkinList.get(i)) > 0
                    && new SimpleDateFormat("yyyy-MM-dd").parse(checkin).compareTo(checkoutList.get(i)) < 0) {
                return false;
            }
            if (new SimpleDateFormat("yyyy-MM-dd").parse(checkout).compareTo(checkinList.get(i)) > 0
                    && new SimpleDateFormat("yyyy-MM-dd").parse(checkout).compareTo(checkoutList.get(i)) < 0) {
                return false;
            }
        }
        boolean result = false;
        for (int i = 0; i < checkinList.size() - 1; i++) {
            if (new SimpleDateFormat("yyyy-MM-dd").parse(checkin).compareTo(checkoutList.get(i)) >= 0
                    && new SimpleDateFormat("yyyy-MM-dd").parse(checkout).compareTo(checkinList.get(i + 1)) <= 0) {
                return true;
            }
        }
        return result;
    }

    public void sendMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("sunnyhotel_canada@yahoo.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        //mailSender.send(message);
    }

    static class SortByDate implements Comparator<Date> {
        @Override
        public int compare(Date a, Date b) {
            return a.compareTo(b);
        }
    }
}
