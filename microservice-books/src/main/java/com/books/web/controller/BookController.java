package com.books.web.controller;

import com.books.dao.BookRepository;
import com.books.entities.Book;
import com.books.web.exceptions.BookNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    /**
     * resume all books in the database
     * @param mc keyword for research function
     * @return  a list of books object
     */
    @GetMapping(value = "/livres")
    public List<Book> bookList(@RequestParam(name = "mc", defaultValue = "")String mc){
        List<Book> books = bookRepository.chercherParTitre("%"+mc+"%");
        return books;
    }

    /**
     * find a book by his name
     * @param id books id
     * @return an object book
     */
    @GetMapping( value = "/livre/{id}")
    public Optional<Book> recupererUnLivre(@PathVariable long id) {

        Optional<Book> book = bookRepository.findById(id);

        if(!book.isPresent())  throw new BookNotFoundException("Le livre correspondant à l'id " + id + " n'existe pas");

        return book;
    }

}
