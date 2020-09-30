package com.books.services.implementations;


import com.books.dao.BookRepository;
import com.books.dao.CopiesRepository;
import com.books.entities.Book;
import com.books.entities.Copy;
import com.books.services.interfaces.BookService;

import com.books.web.exceptions.BookNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CopiesRepository copiesRepository;



    @Override
    public List<Book> findBook(String mc) {
        return  bookRepository.chercherParTitre("%"+mc+"%");
    }

    @Override
    public Book find1Book(Long id) {
        Optional<Book> b = bookRepository.findById(id);
        List<Copy> copies= copiesRepository.findAllByBook_Id(id);
        Book book;
        if(b.isPresent()){
            book=b.get();
            book.setCopies(copies);
            book.setNbTotalCopys(copies.size());
        }else {
            throw new BookNotFoundException("Le livre correspondant à l'id " + id + " n'existe pas");
        }

        return book;
    }
}
