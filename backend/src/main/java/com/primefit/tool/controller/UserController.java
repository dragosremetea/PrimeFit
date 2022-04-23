package com.primefit.tool.controller;

import com.primefit.tool.exceptions.InvalidEmailException;
import com.primefit.tool.model.User;
import com.primefit.tool.service.confirmationtokenservive.ConfirmationTokenService;
import com.primefit.tool.service.userservice.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin("http://localhost:4200/")
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
        User user = userService.getById(id);
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
        return new ResponseEntity<>(userService.saveOrUpdate(user), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody @NotNull User userData) {
        System.out.println(userData);
        Optional<User> optionalUser = userService.findByUsername(userData.getUsername());
        if (optionalUser.isPresent()) {
            if (optionalUser.get().getPassword().equals(userData.getPassword()))
                return ResponseEntity.ok(optionalUser.get());

            throw new UsernameNotFoundException("Password wrong for username: " + userData.getUsername());
        }
        throw new UsernameNotFoundException("Username: " + userData.getUsername() + " not found!");
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
