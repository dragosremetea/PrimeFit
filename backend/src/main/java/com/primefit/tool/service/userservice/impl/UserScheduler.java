package com.primefit.tool.service.userservice.impl;

import com.primefit.tool.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@EnableScheduling
public class UserScheduler {

    @Autowired
    UserService userService;
//
//    @Scheduled(cron = "0 * * ? * *") //every minute
//    public void testFilter()  {
//    }

//    //@Scheduled(cron = "0 * * ? * *") //every minute
//    public void testCart() {

//    }
}