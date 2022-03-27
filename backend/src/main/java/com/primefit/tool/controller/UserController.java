package com.primefit.tool.controller;

import com.primefit.tool.model.User;
import com.primefit.tool.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> getAllUsers(@NotNull Model model) {
        List<User> userList = userService.listAll();
        model.addAttribute("userList", userList);
        return userList;
    }
}
