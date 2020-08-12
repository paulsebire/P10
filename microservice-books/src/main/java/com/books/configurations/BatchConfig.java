package com.books.configurations;


import com.books.batch.TaskOne;
import com.books.dao.EmailRepository;
import com.books.dao.EmpruntRepository;
import com.books.poxies.MicroserviceUtilisateurProxy;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;


@Configuration
@EnableBatchProcessing
public class BatchConfig{

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private EmpruntRepository empruntRepository;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private MicroserviceUtilisateurProxy microserviceUtilisateurProxy;
    @Autowired
    private JavaMailSenderImpl sender;

    @Bean
    public Step stepOne() {
        return steps.get("stepOne")
                .tasklet(new TaskOne(empruntRepository, emailRepository,microserviceUtilisateurProxy, sender))
                .build();
    }


    @Bean
    public Job demoJob(){
        return jobs.get("demoJob")
                .incrementer(new RunIdIncrementer())
                .start(stepOne())
                .build();
    }

}
