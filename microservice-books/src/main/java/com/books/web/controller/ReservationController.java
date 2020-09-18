package com.books.web.controller;


import com.books.dao.CopiesRepository;
import com.books.dao.ReservationRepository;
import com.books.entities.Copy;
import com.books.entities.Reservation;
import com.books.poxies.MicroserviceUtilisateurProxy;
import com.books.services.BibliServiceImpl;
import com.books.web.exceptions.ReservationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class ReservationController {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private BibliServiceImpl bibliService;
    @Autowired
    private CopiesRepository copiesRepository;

    /**
     * find reservations in Db for a specific user
     * @param id id of the user
     * @return alist of reservations
     */
    @GetMapping(value = "/utilisateur/{id}/reservations/")
    public List<Reservation> reservationList(@PathVariable(value = "id")Long id){
        List<Reservation> reservations = reservationRepository.findAllByIdUtilisateurAndCloturerFalseOrderByIdAsc(id);
        if (reservations.isEmpty()) throw new ReservationNotFoundException("Aucune reservation n'est disponible");
        return reservations;
    }

    /**
     * method to give credit time to a reservation
     * @param idResa id of the reservation
     * @param idUser id of the borrower
     * @return a response entity depending on the scenario
     */
    @PostMapping(value = "/utilisateur/{idUser}/reservations/{idResa}/prolonger")
    void prolongerReservation(@PathVariable(value = "idResa")Long idResa,@PathVariable(value = "idUser") Long idUser){
        Optional<Reservation> r= reservationRepository.findById(idResa);
        Date dateDujour = new Date();
        if (r.isPresent()){
            Reservation reservation=r.get();
            if (reservation.getIdUtilisateur()==idUser && reservation.isProlonger()==false){
                reservation.setProlonger(true);
                reservation.setDateRetour(bibliService.ajouter4semaines(reservation.getDateRetour()));
                reservationRepository.save(reservation);
            }
        }
    }

    /**
     * method to create a reservation
     * @param idUser id of the borrower
     * @param idCopy if of the copy borrowed
     * @return a response entity depending on the scenario
     */
    @PostMapping(value = "/utilisateur/{idUser}/copie/{idCopy}/reserver")
     ResponseEntity reserverCopy(@PathVariable(value = "idUser")Long idUser,@PathVariable(value = "idCopy")Long idCopy){

        Optional<Copy> c   = copiesRepository.findById(idCopy);

        if (c.isPresent()){
             Copy copy=c.get();
            if (copy.isDispo()){
                Reservation reservation=new Reservation(copy,new Date());
                reservation.setDateRetour(bibliService.ajouter4semaines(reservation.getDateEmprunt()));
                reservation.setIdUtilisateur(idUser);
                copy.setDispo(false);
                copiesRepository.save(copy);
                reservationRepository.save(reservation);
                return new ResponseEntity<>("reservation effectué", HttpStatus.OK);
            }
        }return new ResponseEntity<>("reservation introuvable", HttpStatus.BAD_REQUEST);
    }

    /**
     * method to close a reservation
     * @param idResa id of the reservation
     * @return  a response entity depending on the scenario
     */
    @PutMapping(value = "/reservation/{idResa}/cloturer")
    ResponseEntity cloturerReservation(@PathVariable(value = "idResa")Long idResa){

        Reservation reservation= reservationRepository.findById(idResa).get();

        if (reservation!=null){
            if (!reservation.isCloturer()){
                Copy copy = copiesRepository.findById(reservation.getCopy().getId()).get();
                reservation.setCloturer(true);
                copy.setDispo(true);
                copiesRepository.save(copy);
                reservationRepository.save(reservation);
                return new ResponseEntity<>("reservation cloturer", HttpStatus.OK);
            }
        }return new ResponseEntity<>("reservation introuvable", HttpStatus.BAD_REQUEST);
    }
}
