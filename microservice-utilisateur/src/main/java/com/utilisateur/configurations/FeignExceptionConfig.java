package com.utilisateur.configurations;


import com.utilisateur.web.exceptions.CustomErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignExceptionConfig {

    @Bean
    public CustomErrorDecoder custumErrorDecoder(){
        return new CustomErrorDecoder();
    }
}