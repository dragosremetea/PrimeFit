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
        return userService.listAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<User> findUserById(@PathVariable("id") Integer id) {
        User user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public void deleteUserById(@PathVariable("id") Integer id) {
        confirmationTokenService.delete(id);
        userService.delete(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<User> saveOrUpdateUser(@RequestBody User newUser, @PathVariable Integer id) {

        Optional<User> optionalUser = Optional.ofNullable(this.userService.findById(id));

        return optionalUser
                .map(user -> {
                    user.setUsername(newUser.getUsername());
                    user.setPassword(newUser.getPassword());
                    user.setFirstName(newUser.getFirstName());
                    user.setLastName(newUser.getLastName());
                    user.setHeight(newUser.getHeight());
                    user.setWeight(newUser.getWeight());
                    user.setEmail(newUser.getEmail());
                    user.setPhoneNumber(newUser.getPhoneNumber());
                    user.setDateOfBirth(newUser.getDateOfBirth());
                    user.setGymSubscriptionStartDate(newUser.getGymSubscriptionStartDate());
                    user.setRoles(newUser.getRoles());

                    return new ResponseEntity<>(userService.saveOrUpdate(user), HttpStatus.CREATED);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return new ResponseEntity<>(userService.saveOrUpdate(newUser), HttpStatus.CREATED);
                });

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

    @PostMapping
    public String register(@RequestBody User request) throws InvalidEmailException, UsernameAlreadyExistsException, EmailAlreadyExistsException, WeakPasswordException {
        return userService.register(request);
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token) {
        return userService.confirmToken(token);
    }
}
