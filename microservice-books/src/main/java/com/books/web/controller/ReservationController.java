package com.books.web.controller;

import com.books.dao.EmpruntRepository;
import com.books.dao.ReservationRepository;
import com.books.entities.Emprunt;
import com.books.entities.Reservation;
import com.books.web.exceptions.ReservationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class ReservationController {


    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private EmpruntRepository empruntRepository;

    /**
     * find reservations in Db for a specific user
     * @param id id of the user
     * @return alist of reservations
     */
    @GetMapping(value = "/utilisateur/{id}/reservations")
    public Set<Reservation> reservervationByUser (@PathVariable(value = "id")Long id){
        List<Reservation> reservationsByUser = reservationRepository.findAllByIdUtilisateurAndEnCoursIsTrueOrderByDateReservationAsc(id);
        Set<Reservation> reservationSet=new HashSet<>();
        if (!reservationsByUser.isEmpty()){
            for (int i=0;i<reservationsByUser.size();i++){
                Reservation reservation =reservationsByUser.get(i);
                List<Reservation> reservationsByBook=reservationRepository.findAllByBookIdAndEnCoursIsTrueOrderByDateReservationAsc(reservation.getBook().getId());
                List<Emprunt> empruntsByBook=empruntRepository.findAllByCopy_BookIdAndCloturerIsFalseOrderByDateRetourAsc(reservation.getBook().getId());
                for (int j=0;j<reservationsByBook.size();j++){
                    if (reservationsByBook.get(j).getIdUtilisateur().equals(id)){
                        reservation.setPosition(j+1);
                        reservation.setDateNextRetour(empruntsByBook.get(0).getDateRetour());
                        reservationSet.add(reservation);
                    }
                }
            }
        } else throw new ReservationNotFoundException("Aucun emprunt n'est disponible");
        return reservationSet;
    }
}
