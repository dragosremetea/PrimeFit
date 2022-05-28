package com.primefit.tool.service.userservice.impl;

import com.primefit.tool.exceptions.*;
import com.primefit.tool.model.ConfirmationToken;
import com.primefit.tool.model.User;
import com.primefit.tool.repository.UserRepository;
import com.primefit.tool.service.confirmationtokenservive.ConfirmationTokenService;
import com.primefit.tool.service.emailsenderservice.EmailService;
import com.primefit.tool.service.userservice.UserService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Service class for managing users.
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final ConfirmationTokenService confirmationTokenService;

    private final EmailService emailService;

    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userRepository.findAll());
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User update(@NotNull User newUser, Integer id) {
        User user = userRepository.findById(id).orElseThrow();

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

        return userRepository.save(user);
    }

    @Override
    public void deleteById(Integer id) {
        // check whether a user exist in a DB or not
        userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        userRepository.deleteById(id);
    }

    /**
     * Check if the username already exists.
     *
     * @param username - username of user
     * @throws UsernameAlreadyExistsException - if a user with the same username already exists
     */
    private void checkIfUsernameAlreadyExists(String username) throws UsernameAlreadyExistsException {
        if (userRepository.findByUsername(username).isPresent())
            throw new UsernameAlreadyExistsException(username);
    }

    @Override
    public boolean IsUsernameAlreadyExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    /**
     * Check if a user email already exists.
     *
     * @param email - email of user
     * @throws EmailAlreadyExistsException - if a user with the same username already exists
     */
    public void checkIfEmailAlreadyExists(String email) throws EmailAlreadyExistsException {
        if (userRepository.findByEmail(email).isPresent())
            throw new EmailAlreadyExistsException(email);
    }

    @Override
    public void checkIfEmailIsValid(String email) throws InvalidEmailException {
        if (!isValidEmailAddress(email))
            throw new InvalidEmailException(email);
    }

    private boolean isValidEmailAddress(String email) {

        boolean result = true;
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException ex) {
            result = false;
        }

        return result;
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

    private String signUpUser(@NotNull User appUser) throws
            UsernameAlreadyExistsException, EmailAlreadyExistsException, WeakPasswordException {

        checkIfEmailAlreadyExists(appUser.getEmail());
        checkIfUsernameAlreadyExists(appUser.getUsername());

        checkPasswordFormat(appUser.getPassword());
        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        userRepository.save(appUser);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(30),
                LocalDateTime.now(),
                appUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    @Override
    public int enableUser(String email) {
        return userRepository.enableUser(email);
    }

    @Override
    public String register(@NotNull User request) throws
            InvalidEmailException, UsernameAlreadyExistsException, EmailAlreadyExistsException, WeakPasswordException {

        checkIfEmailIsValid(request.getEmail());

        String token = signUpUser(
                new User(
                        request.getUsername(),
                        request.getPassword(),
                        request.getFirstName(),
                        request.getLastName(),
                        request.getHeight(),
                        request.getWeight(),
                        request.getEmail(),
                        request.getPhoneNumber(),
                        request.getDateOfBirth(),
                        request.getGymSubscriptionStartDate(),
                        request.getRoles()
                )
        );

        String link = "http://localhost:8080/users/confirm?token=" + token; //url for validating user acc

        emailService.send(request.getEmail(), buildEmail(request.getFirstName(), link));

        return token;
    }

    @Transactional
    @Override
    public String confirmToken(String token) {

        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));

        confirmationTokenService.setConfirmedAt(token);

        enableUser(confirmationToken.getUser().getEmail());

        return "Email confirmed!";
    }

    @Contract(pure = true)
    private @NotNull String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 30 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}

