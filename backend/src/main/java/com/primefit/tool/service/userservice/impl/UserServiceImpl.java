package com.primefit.tool.service.userservice.impl;

import com.primefit.tool.exceptions.EmailAlreadyExistsException;
import com.primefit.tool.exceptions.InvalidEmailException;
import com.primefit.tool.exceptions.ResourceNotFoundException;
import com.primefit.tool.exceptions.UsernameAlreadyExistsException;
import com.primefit.tool.exceptions.WeakPasswordException;
import com.primefit.tool.model.User;
import com.primefit.tool.repository.UserRepository;
import com.primefit.tool.service.userservice.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> listAll() {
        return new ArrayList<>(userRepository.findAll());
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void delete(Integer id) {
        // check whether a user exist in a DB or not
        userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        userRepository.deleteById(id);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public User get(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    public void checkIfUsernameAlreadyExists(String username) throws UsernameAlreadyExistsException {
        if (userRepository.getUserByUsername(username).isPresent())
            throw new UsernameAlreadyExistsException(username);
    }

    public boolean IsUsernameAlreadyExists(String username) {
        return userRepository.getUserByUsername(username).isPresent();
    }

    public void checkIfEmailAlreadyExists(String email) throws EmailAlreadyExistsException {
        if (userRepository.getUserByEmail(email).isPresent())
            throw new EmailAlreadyExistsException(email);
    }

    public void checkIfEmailIsValid(String username) throws InvalidEmailException {
        if (isValidEmailAddress(username))
            throw new InvalidEmailException(username);
    }

    public boolean stringContainsNumber(String s) {
        return Pattern.compile("\\d").matcher(s).find();
    }

    public boolean stringContainsUpperCase(String s) {
        return Pattern.compile("[A-Z]").matcher(s).find();
    }

    public void checkPasswordFormat(String password) throws WeakPasswordException {
        checkIfPasswordContainsAtLeast8Characters(password);
        checkIfPasswordContainsAtLeast1Digit(password);
        checkIfPasswordContainsAtLeast1UpperCase(password);
    }

    private void checkIfPasswordContainsAtLeast8Characters(@NotNull String password) throws WeakPasswordException {
        if (password.length() < 8)
            throw new WeakPasswordException("8 characters");
    }

    private void checkIfPasswordContainsAtLeast1Digit(String password) throws WeakPasswordException {
        if (!stringContainsNumber(password))
            throw new WeakPasswordException("one digit");
    }

    private void checkIfPasswordContainsAtLeast1UpperCase(String password) throws WeakPasswordException {
        if (!stringContainsUpperCase(password))
            throw new WeakPasswordException("one upper case");
    }

    public boolean isValidEmailAddress(String email) {

        boolean result = true;
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException ex) {
            result = false;
        }

        return result;
    }
}

