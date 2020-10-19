package com.books.web.controller;

import com.books.entities.Book;
import com.books.services.implementations.BookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController {

    @Autowired
    private BookServiceImpl bookService;

    /**
     * resume all books in the database
     * @param mc keyword for research function
     * @return  a list of books object
     */
    @GetMapping(value = "/livres")
    public List<Book> bookList(@RequestParam(name = "mc", defaultValue = "")String mc){
        return bookService.findBook(mc);
    }

    /**
     * find a book by his name
     * @param id books id
     * @return an object book
     */
    @GetMapping( value = "/livre/{id}")
    public Book recupererUnLivre(@PathVariable Long id) {
        return bookService.findBookByID(id);
    }

}
