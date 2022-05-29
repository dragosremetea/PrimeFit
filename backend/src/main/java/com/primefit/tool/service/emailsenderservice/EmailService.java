package com.primefit.tool.service.emailsenderservice;

/**
 * Interface used for declaring the methods signatures that can be performed with an email.
 */
public interface EmailService {

    /**
     *
     * @param to - addressee
     * @param email - the email we want to send
     */
    void send(String to, String email);
}
