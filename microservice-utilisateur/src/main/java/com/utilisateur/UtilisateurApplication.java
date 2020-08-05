package com.utilisateur;

import com.utilisateur.dao.UtilisateurRepository;
import com.utilisateur.entities.Utilisateur;
import com.utilisateur.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@EnableDiscoveryClient
@EnableFeignClients("com.utilisateur")
@SpringBootApplication
public class UtilisateurApplication {

	@Autowired
	private UtilisateurRepository utilisateurRepository;

	public static void main(String[] args) {
		SpringApplication.run(UtilisateurApplication.class, args);
	}

	@PostConstruct
	public void postC(){
		if (utilisateurRepository.findAll().isEmpty()){
			Set<RoleEnum> userRole = new HashSet<>();
			userRole.add(RoleEnum.USER);

			Set<RoleEnum> adminRole =new HashSet<>();
			adminRole.add(RoleEnum.USER);
			adminRole.add(RoleEnum.ADMIN);

			Utilisateur user = new Utilisateur("user", "user", "User", "USER","user@email.com", userRole);
			Utilisateur test = new Utilisateur("test", "test", "Test", "TEST","test@email.com", userRole);
			Utilisateur admin = new Utilisateur("admin", "admin", "Admin", "ADMIN","admin@email.com", adminRole);
			utilisateurRepository.save(user);
			utilisateurRepository.save(test);
			utilisateurRepository.save(admin);
		}
	}

}
