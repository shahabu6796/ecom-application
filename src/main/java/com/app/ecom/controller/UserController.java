package com.app.ecom.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.model.User;
import com.app.ecom.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class UserController {
    private final UserService userService;
    @GetMapping("/api/users")
    public ResponseEntity<List<UserResponse>> getUsers()
    {
        return ResponseEntity.ok(userService.fetchAllUsers());
    }
    @GetMapping("/api/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id)
    {
        return userService.findUser(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/api/addUser")
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest)
    {
        userService.createUser(userRequest);
        return ResponseEntity.ok("user created successfully..");
    }
    @PostMapping("/api/updateUser")
    public ResponseEntity<Boolean> updateUser(@RequestParam Long id, @RequestBody User user)
    {
        return ResponseEntity.ok(userService.updateUser(id,user));
    }

}
