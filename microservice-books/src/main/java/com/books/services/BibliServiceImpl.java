package com.books.services;


import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;


@Service
public class BibliServiceImpl implements IBibliService {



    @Override
    public Date ajouter4semaines(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 28);
        return cal.getTime();
    }


}
