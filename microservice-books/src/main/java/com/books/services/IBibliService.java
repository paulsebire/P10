package com.books.services;



import com.books.entities.Book;

import java.util.Date;

public interface IBibliService {

    public Date ajouter4semaines(Date date);

    Book findBook(Long id);
}
