package com.books.web.controller;

import com.books.dao.BookRepository;
import com.books.dao.CopiesRepository;
import com.books.dao.EmpruntRepository;
import com.books.dao.ReservationRepository;
import com.books.entities.Book;
import com.books.entities.Copy;
import com.books.entities.Emprunt;
import com.books.entities.Reservation;
import com.books.services.BibliServiceImpl;
import com.books.web.exceptions.ReservationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class ReservationController {


    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private EmpruntRepository empruntRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BibliServiceImpl bibliService;
    @Autowired
    private CopiesRepository copiesRepository;


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
                        if (empruntsByBook.size()>0){
                            reservation.setDateNextRetour(empruntsByBook.get(0).getDateRetour());
                        } else reservation.setDateNextRetour(new Date());
                        reservationSet.add(reservation);
                    }
                }
            }
        }
        return reservationSet;
    }

    @GetMapping(value = "/utilisateur/{idUser}/livre/{idBook}/reservable")
    boolean livreReservable(@PathVariable(value = "idUser")Long idUser,@PathVariable(value = "idBook")Long idBook){
        Book book = bibliService.findBook(idBook);
        List<Emprunt> emprunts = empruntRepository.livreDejaEmprunteParUtilisateur(idUser,idBook);
        List<Reservation> reservations = reservationRepository.findAllByBookIdAndEnCoursIsTrueOrderByDateReservationAsc(idBook);
        Reservation reservationEncours = reservationRepository.findByBookIdAndIdUtilisateurAndEnCoursTrue(idBook,idUser);
        List<Copy> copiesdispo = copiesRepository.ListCopyDispoByBook(idBook);
        if (emprunts.isEmpty() && reservations.size()<=book.getCopies().size()*2
                && reservationEncours==null && copiesdispo.size()==0 ){
                return true;
            }else return false;

    }

    @GetMapping(value = "/utilisateur/{idUser}/livre/{idBook}/reserver")
    ResponseEntity reserverLivre(@PathVariable(value = "idUser")Long idUser, @PathVariable(value = "idBook")Long idBook){
        Book book =bibliService.findBook(idBook);
        List<Emprunt> emprunts = empruntRepository.livreDejaEmprunteParUtilisateur(idUser,idBook);
        List<Reservation> reservations = reservationRepository.findAllByBookIdAndEnCoursIsTrueOrderByDateReservationAsc(idBook);
        Reservation reservationEncours = reservationRepository.findByBookIdAndIdUtilisateurAndEnCoursTrue(idBook,idUser);
        List<Copy> copiesDispo = copiesRepository.findCopiesByBookIdAndDispoTrue(idBook);
        if (copiesDispo.size()==0){
            if (emprunts.isEmpty()){
                if (reservations.size()<=book.getCopies().size()*2){
                    if (reservationEncours==null){
                        Reservation reservation = new Reservation(book);
                        reservation.setIdUtilisateur(idUser);
                        reservationRepository.save(reservation);
                        return new ResponseEntity<>("livre reservé", HttpStatus.OK);
                    } return new ResponseEntity<>("reservation impossible, livre déjà réservé", HttpStatus.BAD_REQUEST);
                } return new ResponseEntity<>("reservation impossible, file d'attente pleine", HttpStatus.BAD_REQUEST);
            } else return new ResponseEntity<>("reservation impossible, livre déjà emprunté", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>("reservation impossible, des exemplaires sont disponibles", HttpStatus.BAD_REQUEST);

    }

    @PutMapping(value = "/utilisateur/{idUser}/reservation/{id}/annuler")
    ResponseEntity annulerReservation(@PathVariable(value = "idUser")Long idUser,@PathVariable(value = "id")Long id){
       Optional<Reservation> r = reservationRepository.findById(id);
       Reservation reservation= null;
       if (r.isPresent()){
           reservation=r.get();
           if (reservation.getIdUtilisateur().equals(idUser)){
               reservation.setEnCours(false);
               reservationRepository.save(reservation);
               return new ResponseEntity<>("reservation annulée", HttpStatus.OK);
           }return new ResponseEntity<>(" annulation de la reservation impossible, la reservation n'appartient au demandeur", HttpStatus.BAD_REQUEST);
       }return new ResponseEntity<>("annulation de la reservation impossible, la reservation est introuvable", HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/livre/{idBook}/reservations")
    List<Reservation> reservationsByBook(@PathVariable(value = "idBook")Long idBook){
        List<Reservation> reservations= reservationRepository.findAllByBookIdAndEnCoursIsTrueOrderByDateReservationAsc(idBook);
        if (!reservations.isEmpty()){
            return reservations;
        }else return null;
    }

}
