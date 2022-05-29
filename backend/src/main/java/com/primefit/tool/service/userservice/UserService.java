package com.primefit.tool.service.userservice;

import com.primefit.tool.exceptions.EmailAlreadyExistsException;
import com.primefit.tool.exceptions.InvalidEmailException;
import com.primefit.tool.exceptions.UsernameAlreadyExistsException;
import com.primefit.tool.exceptions.WeakPasswordException;
import com.primefit.tool.model.User;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Interface used for declaring the methods signatures that can be performed with a user
 */
public interface UserService {

    /**
     * Get a list with all the trainings
     *
     * @return a list with all the trainings
     */
    List<User> findAll();

    /**
     * Find a specific user based on id.
     *
     * @param id - user id
     * @return found user.
     */
    User findById(Integer id);

    /**
     * Find a specific user based on username
     *
     * @param username - username of user
     * @return found user.
     */
    Optional<User> findByUsername(String username);

    /**
     * Save a user
     *
     * @param user - user entity
     * @return saved user
     */
    User save(User user);

    /**
     * Update all information for a specific user
     *
     * @param newUser - the user who will be persisted
     * @param id      - current user id
     * @return updated user.
     */
    User update(User newUser, Integer id);

    /**
     * Delete a user by a specific id
     *
     * @param id - persisted user id
     */
    void deleteById(Integer id);

    /**
     * Check if the username already exists but no exception is thrown
     *
     * @param username - username of user
     * @return true if the username already exist, otherwise false
     */
    boolean IsUsernameAlreadyExists(String username) throws UsernameAlreadyExistsException;

    /**
     * Verify is the user email is valid
     *
     * @param email - email of user
     * @throws InvalidEmailException - if a user email is invalid
     */
    void checkIfEmailIsValid(String email) throws InvalidEmailException;

    /**
     * Set user acc as enabled
     *
     * @param email of user
     * @return - an integer
     */
    int enableUser(String email);

    /**
     * After the email is confirmed, the token will be set as confirmed the user acc will be enabled
     *
     * @param token - the unique generated token for a new user
     * @return - a confirmation message in a success scenario, else a message of failure if the 30 from registering passed
     */
    String confirmToken(String token);

    /**
     * Based on entered data, a new user will be saved in the DB and a token
     * is generated for validating email within the next 30 minutes from registering.
     *
     * @param request - the user who register in the app
     * @return - a String which contains the unique token generated for the registered user
     * @throws InvalidEmailException          - if a user email is invalid
     * @throws UsernameAlreadyExistsException - if a user with the same username already exists
     * @throws EmailAlreadyExistsException    - if a user with the same email already exists
     * @throws WeakPasswordException          - if user`s password does not fulfill the criteria
     */
    String register(@NotNull User request) throws InvalidEmailException, UsernameAlreadyExistsException, EmailAlreadyExistsException, WeakPasswordException;
}
