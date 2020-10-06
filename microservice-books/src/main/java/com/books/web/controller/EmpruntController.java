package com.books.web.controller;



import com.books.entities.*;
import com.books.services.implementations.EmpruntServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;
import java.util.*;

@RestController
public class EmpruntController {

   @Autowired
   private EmpruntServiceImpl empruntService;

    /**
     * find emprunts in Db for a specific user
     * @param id id of the user
     * @return alist of emprunts
     */
    @GetMapping(value = "/utilisateur/{id}/emprunts")
    public Set<Emprunt> empruntList (@PathVariable(value = "id")Long id){
       return empruntService.empruntByUSer(id);
    }

    /**
     * method to give credit time to an emprunt
     * @param idE id of the emprunt
     * @param idUser id of the borrower
     * @return a response entity depending on the scenario
     */
    @PostMapping(value = "/utilisateur/{idUser}/emprunt/{idE}/prolonger")
    ResponseEntity prolongerEmprunt(@PathVariable(value = "idE")Long idE, @PathVariable(value = "idUser") Long idUser){
       return empruntService.prolongerEmprunt(idE,idUser);
    }

    /**
     * method to create an emprunt
     * @param idUser id of the borrower
     * @param idBook id of the book borrowed
     * @return a response entity depending on the scenario
     */
    @GetMapping(value = "/utilisateur/{idUser}/livre/{idBook}/emprunt/ouvrir")
     ResponseEntity ouvrirEmprunt(@PathVariable(value = "idUser")Long idUser,@PathVariable(value = "idBook")Long idBook){
        return empruntService.ouvrirEmprunt(idUser,idBook);
    }

    /**
     * method to close a emprunt
     * @param idE id of the emprunt
     * @return  a response entity depending on the scenario
     */
    @PutMapping(value = "/emprunt/{idE}/cloturer")
    ResponseEntity cloturerEmprunt(@PathVariable(value = "idE")Long idE) throws MessagingException {
      return empruntService.cloturerEmprunt(idE);
    }


}
