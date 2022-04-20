package com.primefit.tool.service.emailsenderservice;

import org.springframework.stereotype.Repository;

@Repository
public interface EmailService {

    void send(String to, String email);
}
