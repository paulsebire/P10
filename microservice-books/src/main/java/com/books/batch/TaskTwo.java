package com.books.batch;

import com.books.beans.UtilisateurBean;
import com.books.dao.BookRepository;
import com.books.dao.EmailRepository;
import com.books.dao.ReservationRepository;
import com.books.entities.Book;
import com.books.entities.Email;
import com.books.entities.Reservation;
import com.books.poxies.MicroserviceUtilisateurProxy;
import com.books.services.implementations.OutilServiceImpl;
import com.books.services.implementations.EmailServiceImpl;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.text.DateFormat;
import java.util.*;


public class TaskTwo implements Tasklet {

    private final ReservationRepository reservationRepository;
    private final EmailRepository emailRepository;
    private final MicroserviceUtilisateurProxy microserviceUtilisateurProxy;
    private final JavaMailSenderImpl sender;
    private final BookRepository bookRepository;
    private final OutilServiceImpl bibliService;
    private final EmailServiceImpl emailService;
    /**
     * Tache par batch permettant de relancer les utilisateurs qui n'ont pas rendu leurs livres
     */
    public TaskTwo(ReservationRepository reservationRepository, EmailRepository emailRepository,
                   MicroserviceUtilisateurProxy microserviceUtilisateurProxy,
                   BookRepository bookRepository, OutilServiceImpl bibliService, EmailServiceImpl emailService,
                   JavaMailSenderImpl sender) {
        this.reservationRepository = reservationRepository;
        this.emailRepository = emailRepository;
        this.microserviceUtilisateurProxy=microserviceUtilisateurProxy;
        this.sender = sender;
        this.bookRepository=bookRepository;
        this.bibliService=bibliService;

        this.emailService=emailService;
    }


    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        System.out.println("debut du batch de purge de file d'attente");
        Email email = emailRepository.findByName("notification");

        List<Book> allBooks = bookRepository.findAll();
        for (int i=0;i<allBooks.size();i++){
            List<Reservation> reservationsNotifiees = reservationRepository.findAllByBookIdAndEnCoursIsTrueAndNotifiedIsTrueOrderByDateReservationAsc(allBooks.get(i).getId());
            if (!reservationsNotifiees.isEmpty()){
                for (Reservation r:reservationsNotifiees
                     ) {
                    Date deadline= bibliService.ajouter2Jours(r.getDateNotification());
                    if (deadline.before(new Date())){
                        r.setEnCours(false);
                        reservationRepository.save(r);
                        List<Reservation> fileAttente = reservationRepository.findAllByBookIdAndEnCoursIsTrueAndNotifiedIsFalseOrderByDateReservationAsc(r.getBook().getId());
                        if (!fileAttente.isEmpty()){
                            DateFormat shortDateFormat = DateFormat.getDateTimeInstance(
                                    DateFormat.SHORT,
                                    DateFormat.SHORT);
                            String dateDuJour = shortDateFormat.format(new Date());
                            Reservation reservation =fileAttente.get(0);
                            UtilisateurBean utilisateur=microserviceUtilisateurProxy.utilisateurById(reservation.getIdUtilisateur());
                            String text = email.getContenu()
                                    .replace("[USERNAME]",utilisateur.getUsername())
                                    .replace("[LIVRE_TITRE]", reservation.getBook().getName())
                                    .replace("[DATE_RENDU]", dateDuJour);
                            emailService.sendSimpleMessage(utilisateur.getEmail(), email.getObjet(), text);
                            reservation.setNotified(true);
                            reservation.setDateNotification(new Date());
                            reservationRepository.save(reservation);
                        }
                    }
                }

            }

        }




        System.out.println("fin du batch de purge de la file d'attente");

        return RepeatStatus.FINISHED;
    }


}
