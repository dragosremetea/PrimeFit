package com.primefit.tool.service.emailsenderservice;

/**
 * Interface used for declaring the methods signatures that can be performed with an email.
 */
public interface EmailService {

    /**
     *
     * @param to - addressee
     * @param body - the email we want to send
     */
    void send(String to, String body);


    /**
     *
     * @param to - addressee
     * @param body - the email we want to send
     */
    void sendEmailIfSubscriptionExpired(String to, String body);

    /**
     *
     * @param to - addressee
     * @param body - the email we want to send
     */
    void sendDiet(String to, String body);

    /**
     *
     * @param to - addressee
     * @param body - the email we want to send
     */
    void sendTraining(String to, String body);
}
