package com.clientui.security;


import com.clientui.beans.UtilisateurBean;
import com.clientui.proxies.MicroserviceUtilisateurProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurSecurityService implements UserDetailsService {

    @Autowired
    private MicroserviceUtilisateurProxy microserviceUtilisateurProxy;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UtilisateurBean utilisateur = microserviceUtilisateurProxy.utilisateurByUsername(username.toLowerCase());
        if (utilisateur==null){
            throw new UsernameNotFoundException("l'utilisateur "+username+" n'existe pas");
        }else {
            return utilisateur;
        }
    }
}