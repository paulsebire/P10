package com.books.services.interfaces;

import com.books.entities.Emprunt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;


import javax.mail.MessagingException;
import java.util.Set;

public interface EmpruntService {

    Set<Emprunt> empruntByUSer(Long id);
    ResponseEntity prolongerEmprunt(Long idE, Long idUser);
    ResponseEntity ouvrirEmprunt(Long idUser,Long idBook);
    ResponseEntity cloturerEmprunt(Long idE) throws MessagingException;


    }
