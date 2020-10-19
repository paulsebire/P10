package com.books.services.implementations;


import com.books.dao.BookRepository;
import com.books.entities.Book;
import com.books.services.interfaces.OutilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;


@Service
public class OutilServiceImpl implements OutilService {

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
   public Date ajouter2Jours(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 2);
        return cal.getTime();
    }

    @Override
    public Book findBook(Long id) {
        return bookRepository.findById(id).get();
    }
}
