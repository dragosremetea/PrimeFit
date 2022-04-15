package com.primefit.tool.controller;

import com.primefit.tool.model.User;
import com.primefit.tool.service.userservice.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<User> authenticateUser(@RequestBody @NotNull User userData){
        System.out.println(userData);
        User user = userService.get(userData.getId());
        if(user.getPassword().equals(userData.getPassword()))
            return ResponseEntity.ok(user);

        return (ResponseEntity<User>) ResponseEntity.internalServerError();
    }
}
