package com.books.poxies;


import com.books.beans.UtilisateurBean;
import com.books.configurations.FeignConfig;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@FeignClient(name = "zuul-server",contextId = "microserviceUtilisateurProxy",
                configuration= FeignConfig.class)
@RibbonClient("/microservice-utilisateur")
public interface MicroserviceUtilisateurProxy {


    @GetMapping("/microservice-utilisateur/user/{id}")
    UtilisateurBean utilisateurById(@PathVariable(value = "id") Long id);

}
