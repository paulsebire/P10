package com.books.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BatchJob2 {

    @Autowired
    JobLauncher jobLauncher;

    @Qualifier("demoJob2")
    @Autowired
    Job job;


    /**
     * Programmation de la relance par mail tous les jours, heure réglable dans le bootstap.properties du microservice
     * @throws Exception
     */
    @Scheduled(cron = "*/5 * * * * *" )
    public void lendingRevival() throws Exception
    {
        JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(job, params);

    }
}
