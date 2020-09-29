package com.books.web.controller;


import com.books.beans.UtilisateurBean;
import com.books.dao.*;
import com.books.entities.*;
import com.books.poxies.MicroserviceUtilisateurProxy;
import com.books.services.BibliServiceImpl;
import com.books.services.EmailServiceImpl;
import com.books.web.exceptions.EmpruntNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class EmpruntController {

    @Autowired
    private EmpruntRepository empruntRepository;
    @Autowired
    private BibliServiceImpl bibliService;
    @Autowired
    private CopiesRepository copiesRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private EmailServiceImpl emailService;
    @Autowired
    private MicroserviceUtilisateurProxy utilisateurProxy;

    /**
     * find emprunts in Db for a specific user
     * @param id id of the user
     * @return alist of emprunts
     */
    @GetMapping(value = "/utilisateur/{id}/emprunts")
    public Set<Emprunt> empruntList (@PathVariable(value = "id")Long id){
        Set<Emprunt> emprunts = new HashSet<>();
        emprunts=empruntRepository.findAllByIdUtilisateurAndCloturerFalseOrderByDateRetourAsc(id);
        System.out.println("emprunts"+emprunts.toString());
        if (emprunts.isEmpty()) throw new EmpruntNotFoundException("Aucun emprunt n'est disponible");
        return emprunts;
    }

    /**
     * method to give credit time to an emprunt
     * @param idE id of the emprunt
     * @param idUser id of the borrower
     * @return a response entity depending on the scenario
     */
    @PostMapping(value = "/utilisateur/{idUser}/emprunt/{idE}/prolonger")
    void prolongerEmprunt(@PathVariable(value = "idE")Long idE, @PathVariable(value = "idUser") Long idUser){
        Optional<Emprunt> r= empruntRepository.findById(idE);
        Date dateDujour = new Date();
        if (r.isPresent()){
            Emprunt emprunt =r.get();
            if (emprunt.getIdUtilisateur()==idUser && emprunt.isProlonger()==false){
                emprunt.setProlonger(true);
                emprunt.setDateRetour(bibliService.ajouter4semaines(emprunt.getDateRetour()));
                empruntRepository.save(emprunt);
            }
        }
    }

    /**
     * method to create an emprunt
     * @param idUser id of the borrower
     * @param idBook id of the book borrowed
     * @return a response entity depending on the scenario
     */
    @GetMapping(value = "/utilisateur/{idUser}/livre/{idBook}/emprunt/ouvrir")
     ResponseEntity reserverCopy(@PathVariable(value = "idUser")Long idUser,@PathVariable(value = "idBook")Long idBook){

        Book book=bookRepository.findById(idBook).get();
        List<Copy> copiesDispos=copiesRepository.ListCopyDispoByBook(idBook);
        Optional<Copy> c   = copiesRepository.findById(copiesDispos.get(0).getId());
        Reservation reservation = reservationRepository.findByBookIdAndIdUtilisateurAndEnCoursTrue(idBook,idUser);
        if (c.isPresent()){
             Copy copy=c.get();
            if (copy.isDispo()){
                Emprunt emprunt =new Emprunt(copy,new Date());
                emprunt.setDateRetour(bibliService.ajouter4semaines(emprunt.getDateEmprunt()));
                emprunt.setIdUtilisateur(idUser);
                copy.setDispo(false);
                reservation.setEnCours(false);
                reservationRepository.save(reservation);
                copiesRepository.save(copy);
                empruntRepository.save(emprunt);
                return new ResponseEntity<>("emprunt effectué", HttpStatus.OK);
            }
        }return new ResponseEntity<>("emprunt impossible", HttpStatus.BAD_REQUEST);
    }

    /**
     * method to close a emprunt
     * @param idE id of the emprunt
     * @return  a response entity depending on the scenario
     */
    @PutMapping(value = "/emprunt/{idE}/cloturer")
    ResponseEntity cloturerEmprunt(@PathVariable(value = "idE")Long idE) throws MessagingException {
        Email email = emailRepository.findByName("notification");
        Emprunt emprunt = empruntRepository.findById(idE).get();
        UtilisateurBean utilisateur=utilisateurProxy.utilisateurById(emprunt.getIdUtilisateur());
        DateFormat shortDateFormat = DateFormat.getDateTimeInstance(
                DateFormat.SHORT,
                DateFormat.SHORT);
        String dateDuJour = shortDateFormat.format(new Date());
        if (emprunt !=null){
            if (!emprunt.isCloturer()){
                Copy copy = copiesRepository.findById(emprunt.getCopy().getId()).get();
                emprunt.setCloturer(true);
                copy.setDispo(true);
                copiesRepository.save(copy);
                empruntRepository.save(emprunt);
                List<Reservation> fileAttente = reservationRepository.findAllByBookIdAndEnCoursIsTrueAndNotifiedIsFalseOrderByDateReservationAsc(copy.getBook().getId());
                if (!fileAttente.isEmpty()){
                    Reservation reservation =fileAttente.get(0);
                    String text = email.getContenu()
                            .replace("[USERNAME]",utilisateur.getUsername())
                            .replace("[LIVRE_TITRE]", reservation.getBook().getName())
                            .replace("[DATE_RENDU]", dateDuJour);
                    emailService.sendSimpleMessage(utilisateur.getEmail(), email.getObjet(), text);
                    reservation.setNotified(true);
                    reservation.setDateNotification(new Date());
                    reservationRepository.save(reservation);
                }
                return new ResponseEntity<>("emprunt cloturée", HttpStatus.OK);
            }
        }return new ResponseEntity<>("emprunt introuvable", HttpStatus.BAD_REQUEST);
    }


}
