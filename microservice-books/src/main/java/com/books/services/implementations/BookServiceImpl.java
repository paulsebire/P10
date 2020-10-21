package com.books.services.implementations;


import com.books.dao.BookRepository;
import com.books.dao.CopiesRepository;
import com.books.dao.EmpruntRepository;
import com.books.entities.Book;
import com.books.entities.Copy;
import com.books.entities.Emprunt;
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
    @Autowired
    private EmpruntRepository empruntRepository;


    @Override
    public List<Book> findBook(String mc) {
        return  bookRepository.chercherParTitre("%"+mc+"%");
    }

    @Override
    public Book findBookByID(Long id) {
        Optional<Book> b = bookRepository.findById(id);
        List<Copy> copies= copiesRepository.findAllByBook_Id(id);
        List<Emprunt> emprunts = empruntRepository.findAllByCopy_BookIdAndCloturerIsFalseOrderByDateRetourAsc(id);
        Book book;
        if(b.isPresent()){
            book=b.get();
            book.setCopies(copies);
            book.setNbTotalCopys(copies.size());
            if (!emprunts.isEmpty()){
                book.setProchainRetour(emprunts.get(0).getDateRetour());
            }
            bookRepository.save(book);
        }else {
            throw new BookNotFoundException("Le livre correspondant à l'id " + id + " n'existe pas");
        }

        return book;
    }
}
