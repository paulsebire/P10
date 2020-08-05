package com.utilisateur.dao;

import com.utilisateur.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur,Long> {

    public Utilisateur findByUsername(String username);
}
