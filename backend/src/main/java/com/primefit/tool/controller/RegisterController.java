package com.primefit.tool.controller;

import com.primefit.tool.exceptions.EmailAlreadyExistsException;
import com.primefit.tool.exceptions.UsernameAlreadyExistsException;
import com.primefit.tool.exceptions.WeakPasswordException;
import com.primefit.tool.model.User;
import com.primefit.tool.service.userservice.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> saveUser(@RequestBody @NotNull User user) {
        try {
            userService.checkIfUsernameAlreadyExists(user.getUsername());
            userService.checkIfEmailAlreadyExists(user.getEmail());
            userService.checkIfEmailIsValid(user.getEmail());
            userService.checkPasswordFormat(user.getPassword());
        } catch (WeakPasswordException | EmailAlreadyExistsException | UsernameAlreadyExistsException e) {
            System.out.println(e.getMessage());
        } catch (com.primefit.tool.exceptions.InvalidEmailException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }
}
