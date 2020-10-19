package com.books.services.implementations;


import com.books.dao.CopiesRepository;
import com.books.services.interfaces.BookService;
import com.books.services.interfaces.CopyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CopyServiceImpl implements CopyService {


    @Autowired
    private CopiesRepository copiesRepository;


    @Override
    public List listCopyDispoByBookID(Long id) {
        return copiesRepository.ListCopyDispoByBook(id);
    }
}
