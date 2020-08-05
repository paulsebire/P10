package com.books.services;


import com.books.dao.EmailRepository;
import com.books.entities.Email;
import com.books.tools.EmailType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

@Service
public class EmailServiceImpl implements IEmailService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSenderImpl sender;
    @Autowired
    private EmailRepository emailRepository;


    /**
     * Permet l'envoi d'un mail
     *
     * @param email   adresse mail du destinataire
     * @param objet   objet du mail
     * @param contenu message du mail
     * @throws MessagingException
     */
    @Override
    public void sendSimpleMessage(String email, String objet, String contenu) throws MessagingException {

        System.out.println(sender.getHost());
        System.out.println(sender.getPort());

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(email);
        helper.setSubject(objet);
        helper.setText(contenu);

        sender.send(message);

        logger.info("{} email notification has been send to {}", email);
    }

    /**
     * Permet d'envoyer le mail de relance des livres non rendu
     *
     * @param emailTypeList liste des utilisateurs qui n'ont pas rendu leur livres
     * @throws MessagingException
     */
    @Override
    public void sendRevival(List<EmailType> emailTypeList) throws MessagingException {

        Email email = emailRepository.findByName("relance");

        for (EmailType e : emailTypeList) {
            String text = email.getContenu()
                    .replace("[LIVRE_TITRE]", e.getTitre())
                    .replace("[DATE_FIN]", e.getDateDeFinDuPret());
            sendSimpleMessage(e.getEmail(), email.getContenu(), text);
        }
    }

}
