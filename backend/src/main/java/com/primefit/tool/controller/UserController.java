package com.primefit.tool.controller;

import com.primefit.tool.exceptions.InvalidEmailException;
import com.primefit.tool.model.User;
import com.primefit.tool.service.confirmationtokenservive.ConfirmationTokenService;
import com.primefit.tool.service.userservice.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.listAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<User> findUserById(@PathVariable("id") Integer id) {
        User user = userService.get(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") Integer id) {
        confirmationTokenService.delete(id);
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.updateUser(user), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody @NotNull User userData) {
        System.out.println(userData);
        User user = userService.get(userData.getId());
        if (user.getPassword().equals(userData.getPassword()))
            return ResponseEntity.ok(user);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public String register(@RequestBody User request) throws InvalidEmailException {
        return userService.register(request);
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token) {
        return userService.confirmToken(token);
    }
}
