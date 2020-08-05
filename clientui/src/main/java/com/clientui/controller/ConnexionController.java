package com.clientui.controller;

import com.clientui.beans.BookBean;
import com.clientui.proxies.MicroserviceBooksProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
public class ConnexionController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MicroserviceBooksProxy booksProxy;

    @GetMapping(value = "/connexion")
    public ModelAndView loginGet(Model model,
                                 @RequestParam(value = "error", required = false)String error,
                                 @RequestParam(value = "logout", required = false) String logout){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String errorMessage = null;
        if(error != null) {
            logger.error("l'utilisateur a saisi un pseudo ou un mot de passe incorrect");
            errorMessage = "Pseudo ou mot de passe incorrects !";
        }
        if(logout != null) {
            errorMessage = "Vous avez été déconecté avec succès !";
            logger.info("l'utilisateur c'est déconnecté");
        }
        model.addAttribute("errorMessge", errorMessage);

        if(!(auth instanceof AnonymousAuthenticationToken)) {
            return new ModelAndView("redirect:/MonProfile");
        }
        return new ModelAndView("connexion");
    }


    @RequestMapping("/logout")
    public ModelAndView logoutPage (HttpServletRequest request, HttpServletResponse response,Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        List<BookBean> livres =  booksProxy.bookList("");
        model.addAttribute("livres",livres);
        return new ModelAndView("Accueil");
    }

}