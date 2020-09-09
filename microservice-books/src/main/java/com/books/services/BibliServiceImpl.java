package com.books.services;


import com.books.dao.BookRepository;
import com.books.entities.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;


@Service
public class BibliServiceImpl implements IBibliService {

@Autowired
private BookRepository bookRepository;

    @Override
    public Date ajouter4semaines(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 28);
        return cal.getTime();
    }

    @Override
    public Book findBook(Long id) {
        return bookRepository.findById(id).get();
    }
}
