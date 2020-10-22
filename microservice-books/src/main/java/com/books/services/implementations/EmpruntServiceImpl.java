package com.books.services.implementations;


import com.books.beans.UtilisateurBean;
import com.books.dao.*;
import com.books.entities.*;
import com.books.poxies.MicroserviceUtilisateurProxy;
import com.books.services.interfaces.CopyService;
import com.books.services.interfaces.EmpruntService;
import com.books.web.exceptions.EmpruntNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.text.DateFormat;
import java.util.*;


@Service
public class EmpruntServiceImpl implements EmpruntService {


    @Autowired
    private EmpruntRepository empruntRepository;
    @Autowired
    private OutilServiceImpl bibliService;
    @Autowired
    private CopiesRepository copiesRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private EmailServiceImpl emailService;
    @Autowired
    private MicroserviceUtilisateurProxy utilisateurProxy;

    @Override
    public Set<Emprunt> empruntByUSer(Long id) {
        Set<Emprunt> emprunts = new HashSet<>();
        Set<Emprunt> empruntAjour = new HashSet<>();

        emprunts=empruntRepository.findAllByIdUtilisateurAndCloturerFalseOrderByDateRetourAsc(id);
        if (!emprunts.isEmpty()){
            for (Emprunt e:emprunts) {
                if (e.getDateRetour().before(new Date())){
                    e.setProlongeable(false);
                }
                empruntAjour.add(e);
                empruntRepository.save(e);
            }
        }
        return empruntAjour;
    }

    @Override
    public ResponseEntity prolongerEmprunt(Long idE, Long idUser) {
        Optional<Emprunt> r= empruntRepository.findById(idE);
        if (r.isPresent()){
            Emprunt emprunt =r.get();
            if (emprunt.getIdUtilisateur()==idUser&& emprunt.isProlongeable() && emprunt.isProlonger()==false && emprunt.getDateRetour().after(new Date())){
                emprunt.setProlonger(true);
                emprunt.setProlongeable(false);
                emprunt.setDateRetour(bibliService.ajouter4semaines(emprunt.getDateRetour()));
                empruntRepository.save(emprunt);
                return new ResponseEntity<>("emprunt prolongé", HttpStatus.OK);
            }else return new ResponseEntity<>("prolongation impossible", HttpStatus.BAD_REQUEST);
        }else return new ResponseEntity<>("emprunt introuvable", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity ouvrirEmprunt(Long idUser, Long idBook) {
        List<Copy> copiesDispos=copiesRepository.ListCopyDispoByBook(idBook);
        Optional<Copy> c   = copiesRepository.findById(copiesDispos.get(0).getId());
        Reservation reservation = reservationRepository.findByBookIdAndIdUtilisateurAndEnCoursTrue(idBook,idUser);
        List<Emprunt> emprunts = empruntRepository.livreDejaEmprunteParUtilisateur(idUser,idBook);
        if (c.isPresent()){
            Copy copy=c.get();
            if (copy.isDispo() && emprunts.isEmpty()){
                Emprunt emprunt =new Emprunt(copy,new Date());
                emprunt.setDateRetour(bibliService.ajouter4semaines(emprunt.getDateEmprunt()));
                emprunt.setIdUtilisateur(idUser);
                copy.setDispo(false);
                copiesRepository.save(copy);
                empruntRepository.save(emprunt);
                if (reservation!=null){
                    reservation.setEnCours(false);
                    reservationRepository.save(reservation);
                }
                return new ResponseEntity<>("emprunt effectué", HttpStatus.OK);
            }
        }return new ResponseEntity<>("emprunt impossible", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity cloturerEmprunt(Long idE) throws MessagingException {
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
                    UtilisateurBean reservant= utilisateurProxy.utilisateurById(reservation.getIdUtilisateur());
                    String text = email.getContenu()
                            .replace("[USERNAME]",reservant.getUsername())
                            .replace("[LIVRE_TITRE]", reservation.getBook().getName())
                            .replace("[DATE_RENDU]", dateDuJour);
                    emailService.sendSimpleMessage(reservant.getEmail(), email.getObjet(), text);
                    reservation.setNotified(true);
                    reservation.setDateNotification(new Date());
                    reservationRepository.save(reservation);
                }
                return new ResponseEntity<>("emprunt cloturé", HttpStatus.OK);
            }
        }return new ResponseEntity<>("emprunt introuvable", HttpStatus.BAD_REQUEST);
    }
}
