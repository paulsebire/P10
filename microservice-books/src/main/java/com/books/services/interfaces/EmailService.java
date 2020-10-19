package com.books.services.interfaces;

import com.books.entities.Email;
import com.books.tools.EmailType;

import javax.mail.MessagingException;
import java.util.List;

public interface EmailService {

    void sendSimpleMessage(String email, String objet, String contenu) throws MessagingException;

    void sendRevival(List<EmailType> emailTypeList) throws MessagingException;
}
