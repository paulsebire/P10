package com.books.configurations;


import com.books.batch.TaskOne;
import com.books.batch.TaskTwo;
import com.books.dao.BookRepository;
import com.books.dao.EmailRepository;
import com.books.dao.EmpruntRepository;
import com.books.dao.ReservationRepository;
import com.books.poxies.MicroserviceUtilisateurProxy;
import com.books.services.BibliServiceImpl;
import com.books.services.EmailServiceImpl;
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
public class BatchConfig2 {

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
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BibliServiceImpl bibliService;
    @Autowired
    private EmailServiceImpl emailService;


    @Bean
    public Step stepTwo(){
        return steps.get("stepTwo")
                .tasklet(new TaskTwo( reservationRepository,  emailRepository,
                         microserviceUtilisateurProxy,
                         bookRepository, bibliService,  emailService,
                         sender))
                .build();
    }


    @Bean
    public Job demoJob2(){
        return jobs.get("demoJob2")
                .incrementer(new RunIdIncrementer())
                .start(stepTwo())
                .build();
    }

}
