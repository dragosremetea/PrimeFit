package com.primefit.tool.service.userservice;

import com.primefit.tool.exceptions.EmailAlreadyExistsException;
import com.primefit.tool.exceptions.InvalidEmailException;
import com.primefit.tool.exceptions.UsernameAlreadyExistsException;
import com.primefit.tool.exceptions.WeakPasswordException;
import com.primefit.tool.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserService  {

    List<User> listAll();

    User save(User user);

    void delete(Integer id);

    User updateUser(User user);

    User get(Integer id);

    void checkIfUsernameAlreadyExists(String username) throws UsernameAlreadyExistsException;

    boolean IsUsernameAlreadyExists(String username) throws UsernameAlreadyExistsException;

    void checkPasswordFormat(String password) throws WeakPasswordException;

    void checkIfEmailAlreadyExists(String email) throws EmailAlreadyExistsException;

    void checkIfEmailIsValid(String username) throws InvalidEmailException;
}
