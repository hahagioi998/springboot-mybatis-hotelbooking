package com.desmond.HotelBooking.controller;

import com.desmond.HotelBooking.entity.User;
import com.desmond.HotelBooking.service.UserService;
import com.desmond.HotelBooking.utils.GenUUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RegisterController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        try{
            User usernameExists = userService.findByUsername(user.getUsername());

            if(usernameExists != null) {
                return new ResponseEntity<>("Username already exists", HttpStatus.CONFLICT);
            } else {
                if (user.getPassword().equals(user.getConfirmPassword())){
                    user.setId(new GenUUID().getUUID());
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    userService.addUser(user);
                    return new ResponseEntity<>(HttpStatus.CREATED);
                }
                return new ResponseEntity<>("Passwords are not same", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
