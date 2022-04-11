package com.pogrammerlin.macroknapsack.controller;

import com.pogrammerlin.macroknapsack.dto.response.UserDetailResponse;
import com.pogrammerlin.macroknapsack.dto.response.UsersDetailResponse;
import com.pogrammerlin.macroknapsack.model.User;
import com.pogrammerlin.macroknapsack.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserActionController {
    private UserService userService;

    @GetMapping("/all")
    public ResponseEntity<UsersDetailResponse> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/create")
    public ResponseEntity<UserDetailResponse> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }
}
