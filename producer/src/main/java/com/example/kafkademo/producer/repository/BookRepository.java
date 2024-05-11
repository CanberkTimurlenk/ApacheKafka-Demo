package com.example.kafkademo.producer.repository;

import com.example.kafkademo.producer.entity.Book;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends ListCrudRepository<Book, Long> {
}
