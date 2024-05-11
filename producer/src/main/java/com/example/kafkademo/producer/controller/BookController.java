package com.example.kafkademo.producer.controller;

import com.example.kafkademo.producer.dto.BookRequestDto;
import com.example.kafkademo.producer.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody BookRequestDto bookRequestDto) {

        bookService.Save(bookRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
