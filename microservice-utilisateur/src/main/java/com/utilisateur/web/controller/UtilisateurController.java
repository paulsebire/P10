package com.utilisateur.web.controller;

import com.utilisateur.dao.UtilisateurRepository;
import com.utilisateur.entities.Utilisateur;
import com.utilisateur.web.exceptions.UtilisateurNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class UtilisateurController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    /**
     * resume all users
     * @return a list of all users
     */
    @GetMapping("/utilisateurs")
    public List<Utilisateur> utilisateursList (){
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll();
        return utilisateurList;
    }

    /**
     * find a user by his username
     * @param username name of the user
     * @return an object user
     */
    @GetMapping("/utilisateur/{username}")
    public Utilisateur utilisateurByUsername (@PathVariable String username){
        Utilisateur utilisateur  = utilisateurRepository.findByUsername(username.toLowerCase());
        return utilisateur;
    }

    /**
     * find a user by his id
     * @param id id of the user
     * @return an object user
     */
    @GetMapping("/user/{id}")
    public Utilisateur utilisateurById (@PathVariable Long id){
        Optional<Utilisateur> u = utilisateurRepository.findById(id);
        Utilisateur utilisateur = null;
        if (u.isPresent()){
            utilisateur=u.get();
        }
        return utilisateur;
    }

}
