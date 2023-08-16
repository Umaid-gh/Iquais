package com.aiykr.iquais.service;

import com.aiykr.iquais.exception.IquaisException;

import javax.mail.MessagingException;

public interface EmailService {

    void sendEmail(String to, String cc, String subject, String content) throws MessagingException, IquaisException;
}
