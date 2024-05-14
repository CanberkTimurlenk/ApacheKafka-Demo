package com.example.kafkademo.consumer.service.kafka.consumer;

import com.example.kafkademo.consumer.dto.MailDto;
import com.example.kafkademo.consumer.service.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BookConsumer {

    private static final Logger logger = LoggerFactory.getLogger(BookConsumer.class);

    private final ObjectMapper objectMapper;
    private final EmailService emailService;

    @KafkaListener(topics = "mytopic", groupId = "bookAdd")
    @RetryableTopic(attempts = "1", dltStrategy = DltStrategy.FAIL_ON_ERROR)
    public void listenAddedBook(String mailStructure) {

        MailDto mail;

        try {
            mail = objectMapper.readValue(mailStructure, MailDto.class);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        emailService.sendMail(mail.to(), mail.mailBody());
    }

    @DltHandler
    public void handleDltBookAdded(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Payload MailDto mailDto) {
        emailService.sendMail(mailDto.to(), mailDto.mailBody());
    }
}
