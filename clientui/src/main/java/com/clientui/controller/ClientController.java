package com.clientui.controller;

import com.clientui.beans.*;
import com.clientui.proxies.MicroserviceBooksProxy;

import com.clientui.proxies.MicroserviceUtilisateurProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;




@Controller
public class ClientController {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MicroserviceBooksProxy booksProxy;
    @Autowired
    private MicroserviceUtilisateurProxy utilisateurProxy;

    private boolean reservable=false;


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
    @GetMapping("/livre/{id}")
    public String ficheLivre(@PathVariable Long id,  Model model){

        BookBean livre = booksProxy.recupererUnLivre(id);
        List<CopyBean> copies = booksProxy.CopiesDispo(id);
        model.addAttribute("livre", livre);
        model.addAttribute("copiesDispos",copies);
        List<ReservationBean> reservationsEnCours = booksProxy.reservationsByBook(id);
        if (reservationsEnCours!=null){
            model.addAttribute("nbResaEnCours",reservationsEnCours.size());
        } else model.addAttribute("nbResaEnCours",0);

        UtilisateurBean utilisateur = getUserConnected();
        if (utilisateur!=null){
            reservable=booksProxy.livreReservable(getUserConnected().getIdUser(),id);
        }
        model.addAttribute("reservable",reservable);
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
    @GetMapping("/MonProfile/Emprunts")
    public String monProfileMesEmprunts (Model model){
        Set<EmpruntBean> emprunts = booksProxy.empruntList(getUserConnected().getIdUser());
        model.addAttribute("emprunts",emprunts);
        boolean mesEmprunts=true;
        model.addAttribute("mesEmprunts",mesEmprunts);
        return "MonProfile";
    }
    /**
     * method to access to personal space
     * @param model model
     * @return the view monprofile
     */
    @GetMapping("/MonProfile/Reservations")
    public String monProfileMesReservations (Model model){
        Set<ReservationBean> reservations = booksProxy.reservationsByUser(getUserConnected().getIdUser());
        model.addAttribute("reservations",reservations);
        boolean mesEmprunts=false;
        model.addAttribute("mesEmprunts",mesEmprunts);
        return "MonProfile";
    }

    /**
     * mehtod to give extra time to a emprunt
     * @param id  id of the emprunt
     * @return the view monprofile
     */
    @GetMapping("/emprunt/{id}/prolonger")
    public String prolongerEmprunt(@PathVariable(value = "id")Long id){
        UtilisateurBean utilisateur = getUserConnected();
        booksProxy.prolongerEmprunt(id,utilisateur.getIdUser());
        return "redirect:/MonProfile/Emprunts";
    }

    private UtilisateurBean getUserConnected(){

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UtilisateurBean){
           return  (UtilisateurBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return null;
    }

    @GetMapping("/livre/{id}/reserver")
    public String reserver(Model model,@PathVariable Long id){
        reservable=false;
        UtilisateurBean utilisateur = getUserConnected();
        if (utilisateur!=null){
            booksProxy.reserverLivre(utilisateur.getIdUser(),id);
            Set<ReservationBean> reservations = booksProxy.reservationsByUser(getUserConnected().getIdUser());
            model.addAttribute("reservations",reservations);
            boolean mesEmprunts=false;
            model.addAttribute("mesEmprunts",mesEmprunts);
            return "redirect:/MonProfile/Reservations";
        }else{
            List<BookBean> livres =  booksProxy.bookList("");
            model.addAttribute("livres", livres);
            model.addAttribute("mc","");
            log.info("Récupération de la liste des livres");
            return "Accueil";

        }
    }

    @GetMapping("reservation/{id}/annuler")
    public String annulerReservation (Model model,@PathVariable(value = "id")Long id){
        UtilisateurBean utilisateur=getUserConnected();
        booksProxy.annulerReservation(utilisateur.getIdUser(),id);
        Set<ReservationBean> reservations = booksProxy.reservationsByUser(getUserConnected().getIdUser());
        model.addAttribute("reservations",reservations);
        boolean mesEmprunts=false;
        model.addAttribute("mesEmprunts",mesEmprunts);
        return "redirect:/MonProfile/Reservations";
    }
}
