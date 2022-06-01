package com.primefit.tool.service.userservice.impl;

import com.primefit.tool.model.User;
import com.primefit.tool.service.emailsenderservice.EmailService;
import com.primefit.tool.service.userservice.UserService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.List;

/**
 * Class used to remind users that their subscription has expired.
 */
@Component
@EnableScheduling
@AllArgsConstructor
public class UserScheduler {

    private UserService userService;

    private EmailService emailService;

    //@Scheduled(cron = "0 * * ? * *") //every minute reminder
    @Scheduled(cron = "0 0 8 * * *")  //every day reminder at 8 o`clock
    public void sendEmailIfSubscriptionExpired() {
        List<User> userList = userService.findAll();
        String emailBodyMessage = "Your subscription has expired. \nIf you want to resubscribe please contact an operator!";

        for (User user : userList) {
            if (user.getGymSubscriptionStartDate().isBefore(ChronoLocalDate.from(LocalDateTime.now().minusDays(30)))) {
                emailService.sendEmailIfSubscriptionExpired(user.getEmail(), emailBodyMessage);
            }
        }
    }
}