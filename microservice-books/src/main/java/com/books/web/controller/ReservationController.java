package com.books.web.controller;


import com.books.entities.Reservation;
import com.books.services.implementations.ReservationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class ReservationController {


    @Autowired
    private ReservationServiceImpl reservationService;



    @GetMapping(value = "/utilisateur/{id}/reservations")
    public Set<Reservation> reservervationByUser (@PathVariable(value = "id")Long id){
        return reservationService.reservervationByUser(id);

    }

    @GetMapping(value = "/utilisateur/{idUser}/livre/{idBook}/reservable")
    boolean livreReservable(@PathVariable(value = "idUser")Long idUser,@PathVariable(value = "idBook")Long idBook){
        return reservationService.livreReservable(idUser,idBook);
    }

    @GetMapping(value = "/utilisateur/{idUser}/livre/{idBook}/reserver")
    ResponseEntity reserverLivre(@PathVariable(value = "idUser")Long idUser, @PathVariable(value = "idBook")Long idBook){
      return reservationService.reserverLivre(idUser,idBook);
    }

    @PutMapping(value = "/utilisateur/{idUser}/reservation/{id}/annuler")
    ResponseEntity annulerReservation(@PathVariable(value = "idUser")Long idUser,@PathVariable(value = "id")Long id){
       return reservationService.annulerReservation(idUser, id);
    }

    @GetMapping(value = "/livre/{idBook}/reservations")
    List<Reservation> reservationsByBook(@PathVariable(value = "idBook")Long idBook){
       return reservationService.reservationsByBook(idBook);
    }

}
