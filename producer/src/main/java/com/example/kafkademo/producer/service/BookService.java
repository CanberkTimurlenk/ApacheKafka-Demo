package com.example.kafkademo.producer.service;

import com.example.kafkademo.producer.dto.BookRequestDto;
import com.example.kafkademo.producer.dto.MailDto;
import com.example.kafkademo.producer.entity.Book;
import com.example.kafkademo.producer.repository.BookRepository;
import com.example.kafkademo.producer.service.kafka.producer.MailServiceProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final MailServiceProducer mailServiceProducer;

    public void Save(BookRequestDto bookRequestDto) {
        Book book = new Book();

        book.setPageCount(bookRequestDto.pageCount());
        book.setTitle(bookRequestDto.title());

        if(bookRepository.save(book).getId() > 0)
        {
            MailDto mailDto = new MailDto(
                    "A book was created with title name"+ bookRequestDto.title(),
                    "admin@mail.com");

            mailServiceProducer.sendMessageToKafka(mailDto);
        }

        else{
            throw new RuntimeException("An error occured during save process");
        }
    }
}