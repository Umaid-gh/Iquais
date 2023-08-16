package com.aiykr.iquais.service.impl;

import com.aiykr.iquais.exception.IquaisException;
import com.aiykr.iquais.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String to, String cc, String subject, String content) throws MessagingException, IquaisException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
                helper.setTo(to);
                helper.setCc(cc);
                helper.setSubject(subject);
                helper.setText(content, true); // Enable HTML content if needed
                javaMailSender.send(message);
                log.info("Email sent to {} and Guardian {}", to, cc);
        } catch (MessagingException msgEx) {
            throw new IquaisException(HttpStatus.UNAUTHORIZED, "IQ003", "Error while sending Email");
        }
    }
}
