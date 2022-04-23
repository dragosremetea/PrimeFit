package com.primefit.tool.service.userservice;

import com.primefit.tool.exceptions.EmailAlreadyExistsException;
import com.primefit.tool.exceptions.InvalidEmailException;
import com.primefit.tool.exceptions.UsernameAlreadyExistsException;
import com.primefit.tool.exceptions.WeakPasswordException;
import com.primefit.tool.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserService {

    List<User> listAll();

    User getById(Integer id);

    Optional<User> findByUsername(String username);

    User saveOrUpdate(User user);

    void delete(Integer id);

    void checkIfUsernameAlreadyExists(String username) throws UsernameAlreadyExistsException;

    boolean IsUsernameAlreadyExists(String username) throws UsernameAlreadyExistsException;

    void checkPasswordFormat(String password) throws WeakPasswordException;

    void checkIfEmailAlreadyExists(String email) throws EmailAlreadyExistsException;

    void checkIfEmailIsValid(String username) throws InvalidEmailException;

    String signUpUser(User appUser);

    int enableAppUser(String email);

    String confirmToken(String token);

    String register(@NotNull User request) throws InvalidEmailException;
}
