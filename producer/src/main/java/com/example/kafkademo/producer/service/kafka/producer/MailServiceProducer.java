package com.example.kafkademo.producer.service.kafka.producer;

import com.example.kafkademo.producer.dto.MailDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceProducer {

    private static final Logger logger = LoggerFactory.getLogger(MailServiceProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${topic.mail}")
    private String mailTopic;

    public void sendMessageToKafka(MailDto requestDto) {

        String valueAsString = "";

        try {

            // write the dto as json
            valueAsString = objectMapper.writeValueAsString(requestDto);

            // Send method returns a CompletableFuture
            var future = kafkaTemplate.send(mailTopic, valueAsString);

            String finalValueAsString = valueAsString;

            future.whenComplete((result, ex) -> {

                if (ex == null) {
                    // If the message was sent then this block will be executed
                    logger.info("Sent message=[{}] with offset=[{}]", finalValueAsString, result.getRecordMetadata().offset());

                } else {
                    // If an exception had thrown, this block will be executed
                    logger.error("Unable to send message=[{}] due to : {}", finalValueAsString, ex.getMessage());
                }
            });

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}