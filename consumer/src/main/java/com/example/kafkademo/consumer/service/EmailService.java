package com.example.kafkademo.consumer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${book.mail.from}")
    private String bookMailFrom;

    private final JavaMailSender emailSender;

    public void sendMail(String to, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(bookMailFrom);
        message.setTo(to);
        message.setSubject("New Book Added!");
        message.setText(text);

        emailSender.send(message);
    }
}
