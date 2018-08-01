package com.marcus.springboot.controller;

import com.marcus.springboot.exception.BookMisMatchException;
import com.marcus.springboot.exception.BookNotFoundException;
import com.marcus.springboot.entity.Book;
import com.marcus.springboot.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookRepository repository;

    @GetMapping
    public Iterable<Book> findAll(){
        return repository.findAll();
    }

    @GetMapping("title/{bookTitle}")
    public List findByTitle(@PathVariable String bookTitle){
        return repository.findByTitle(bookTitle);
    }

    @GetMapping("/{id}")
    public Book findOne(@PathVariable long id){
        return repository.findById(id).orElseThrow(BookNotFoundException::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book){
        return repository.save(book);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        repository.findById(id).orElseThrow(BookNotFoundException::new);
        repository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Book update(@RequestBody Book book,@PathVariable Long id){
        if(book.getId()!=id){
            throw new BookMisMatchException();
        }
        repository.findById(id).orElseThrow(BookNotFoundException::new);
        return repository.save(book);
    }

}
