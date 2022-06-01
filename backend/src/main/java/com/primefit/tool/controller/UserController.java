package com.primefit.tool.controller;

import com.primefit.tool.exceptions.EmailAlreadyExistsException;
import com.primefit.tool.exceptions.InvalidEmailException;
import com.primefit.tool.exceptions.UsernameAlreadyExistsException;
import com.primefit.tool.exceptions.WeakPasswordException;
import com.primefit.tool.model.User;
import com.primefit.tool.service.confirmationtokenservive.ConfirmationTokenService;
import com.primefit.tool.service.userservice.UserService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
@CrossOrigin("http://localhost:4200/")
public class UserController {

    private UserService userService;

    private ConfirmationTokenService confirmationTokenService;

    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @PostMapping("")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        LocalDateTime now = LocalDateTime.now();

        user.setGymSubscriptionStartDate(LocalDate.from(now));
        user.setEnabled(true);
        user.setLocked(false);
        User savedUser = userService.save(user);

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("/username")
    public ResponseEntity<User> findUserByUsername(@RequestParam(value = "username") String username) {
        Optional<User> user = userService.findByUsername(username);

        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("{id}")
    public ResponseEntity<User> findUserById(@PathVariable("id") Integer id) {
        User user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable("id") Integer id) {
        confirmationTokenService.delete(id);
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@RequestBody User newUser, @PathVariable Integer id) {
        userService.update(newUser, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody @NotNull User userData) {
        Optional<User> optionalUser = userService.findByUsername(userData.getUsername());

        if (optionalUser.isPresent()) {
            if (passwordEncoder.matches(userData.getPassword(), optionalUser.get().getPassword())) {   //verify if the plain text password match the encoded password from DB
                return ResponseEntity.ok(optionalUser.get());
            }
        }

        throw new UsernameNotFoundException("Username: " + userData.getUsername() + " not found!");
    }

//    @PostMapping
//    public String register(@RequestBody User request) throws InvalidEmailException, UsernameAlreadyExistsException, EmailAlreadyExistsException, WeakPasswordException {
//        return userService.register(request);
//    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token) {
        return userService.confirmToken(token);
    }
}
