package com.primefit.tool.service;

import com.primefit.tool.model.User;
import com.primefit.tool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> listAll() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void delete(Integer id) {
        userRepository.deleteById(id);
    }

    public Optional<User> get(Integer id) {
        return userRepository.findById(id);
    }

    public boolean checkIfUsernameAlreadyExists(String username) {
        return userRepository.getUserByUsername(username).isPresent();
    }
}
