package com.aiykr.iquais.service;

import com.aiykr.iquais.exception.IquaisException;

import javax.mail.MessagingException;

/**
 * Service interface for sending emails.
 */
public interface EmailService {

    /**
     * Sends an email with the provided details.
     *
     * @param to      The recipient email address.
     * @param cc      The CC (carbon copy) email address.
     * @param subject The subject of the email.
     * @param content The content of the email.
     * @throws MessagingException If an error occurs while sending the email.
     * @throws IquaisException    If there is a custom application-specific exception related to sending emails.
     */
    void sendEmail(String to, String cc, String subject, String content) throws MessagingException, IquaisException;
}

