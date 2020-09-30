package com.books.services.interfaces;

import com.books.entities.Reservation;
import org.springframework.http.ResponseEntity;


import java.util.List;
import java.util.Set;

public interface ReservationService {
    Set<Reservation> reservervationByUser(Long id);
    boolean livreReservable(Long idUser,Long idBook);
    ResponseEntity reserverLivre(Long idUser,Long idBook);
    ResponseEntity annulerReservation(Long idUser,Long id);
    List<Reservation> reservationsByBook(Long idBook);
}
