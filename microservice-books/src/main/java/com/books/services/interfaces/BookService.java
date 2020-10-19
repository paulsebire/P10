package com.books.services.interfaces;

import com.books.entities.Book;


import java.util.List;

public interface BookService {

    List<Book> findBook (String mc);
    Book findBookByID(Long id);
}
