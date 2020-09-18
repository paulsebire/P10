package com.clientui.controller;

import com.clientui.beans.BookBean;
import com.clientui.beans.CopyBean;
import com.clientui.beans.ReservationBean;
import com.clientui.beans.UtilisateurBean;
import com.clientui.proxies.MicroserviceBooksProxy;

import com.clientui.proxies.MicroserviceUtilisateurProxy;
import com.netflix.discovery.converters.Auto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;




@Controller
public class ClientController {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MicroserviceBooksProxy booksProxy;
    @Autowired
    private MicroserviceUtilisateurProxy utilisateurProxy;

    /**
     * method to generate the home page
     * @param model model
     * @param mc keyword of research function
     * @return the view  Accueil
     */
    @GetMapping("/")
    public String accueil(Model model,@RequestParam(name = "mc", defaultValue = "")String mc){
        List<BookBean> livres =  booksProxy.bookList(mc);
        model.addAttribute("livres", livres);
        model.addAttribute("mc",mc);
        log.info("Récupération de la liste des livres");
        return "Accueil";
    }


    /**
     * method to  display a specific book
     * @param id id of the book
     * @param model model
     * @return the view fichelivre
     */
    @GetMapping("/fiche-livre/{id}")
    public String ficheLivre(@PathVariable Long id,  Model model){

        BookBean livre = booksProxy.recupererUnLivre(id);
        List<CopyBean> copies = booksProxy.CopiesDispo(id);
        System.out.println("copies"+copies);
        model.addAttribute("livre", livre);
        model.addAttribute("copies",copies);
        log.trace("Récupération de la fiche d'un livre");
        return "FicheLivre";
    }

    /**
     * method  to acces a list  of all users
     * @param model model
     * @return the view users
     */
    @GetMapping("/users")
    public String allUsers(Model model){
        List<UtilisateurBean> userList= utilisateurProxy.utilisateurList();
        model.addAttribute("userList",userList);
        return "users";
    }

    /**
     * method to access to personal space
     * @param model model
     * @return the view monprofile
     */
    @GetMapping("/MonProfile")
    public String monProfile (Model model){
        UtilisateurBean utilisateur = (UtilisateurBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<ReservationBean> reservations = booksProxy.reservationList(utilisateur.getIdUser());
        model.addAttribute("reservations",reservations);
        return "MonProfile";
    }

    /**
     * mehtod to give extra time to a reservation
     * @param id  id of the reservation
     * @return the view monprofile
     */
    @GetMapping("/reservation/{id}/prolonger")
    public String prolongerResa(@PathVariable(value = "id")Long id){
        UtilisateurBean utilisateur = (UtilisateurBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("id"+utilisateur.getIdUser());
        booksProxy.prolongerReservation(id,utilisateur.getIdUser());
        return "redirect:/MonProfile";
    }
}
