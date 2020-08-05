package com.books.batch;

import com.books.beans.UtilisateurBean;
import com.books.dao.EmailRepository;
import com.books.dao.ReservationRepository;
import com.books.entities.Email;
import com.books.entities.Reservation;
import com.books.poxies.MicroserviceUtilisateurProxy;
import com.books.tools.EmailType;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


public class TaskOne implements Tasklet {

    private final ReservationRepository reservationRepository;
    private final EmailRepository emailRepository;
    private final MicroserviceUtilisateurProxy microserviceUtilisateurProxy;
    private final JavaMailSenderImpl sender;

    /**
     * Tache par batch permettant de relancer les utilisateurs qui n'ont pas rendu leurs livres
     */
    public TaskOne(ReservationRepository reservationRepository, EmailRepository emailRepository, MicroserviceUtilisateurProxy microserviceUtilisateurProxy, JavaMailSenderImpl sender) {
        this.reservationRepository = reservationRepository;
        this.emailRepository = emailRepository;
        this.microserviceUtilisateurProxy=microserviceUtilisateurProxy;
        this.sender = sender;
    }


    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        System.out.println("debut du batch de relance");

        SimpleDateFormat oldFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        Set<Reservation> reservationList = reservationRepository.findByCloturerFalseAndDateRetourBefore(date);

        ArrayList<EmailType> emailType = new ArrayList<>();

        if (reservationList.size() > 0){
            for (Reservation res : reservationList) {
                UtilisateurBean utilisateurBean = microserviceUtilisateurProxy.utilisateurById(res.getIdUtilisateur());
                if (res.isCloturer()==false&&utilisateurBean!=null&&res.getDateRetour().compareTo(date)<0)
                emailType.add(new EmailType(utilisateurBean.getEmail(),
                            res.getCopy().getBook().getName(), oldFormat.format(res.getDateRetour()),utilisateurBean.getUsername()));

            }
            List<EmailType> emailList = new ArrayList<>(emailType);
            this.sendRevival(emailList);
        }


        System.out.println("fin du batch de relance");

        return RepeatStatus.FINISHED;
    }

    public void sendRevival(List<EmailType> emailList){

        Email email = emailRepository.findByName("relance");


        for (EmailType e: emailList) {
            String text = email.getContenu()
                    .replace("[USERNAME]", e.getUsername())
                    .replace("[LIVRE_TITRE]", e.getTitre())
                    .replace("[DATE_FIN]", e.getDateDeFinDuPret());
            this.sendSimpleMessage(e.getEmail(),email.getObjet(),text);
        }
    }

    private void sendSimpleMessage(String email, String objet, String contenu) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(objet);
        message.setText(contenu);
        sender.send(message);
    }

}
