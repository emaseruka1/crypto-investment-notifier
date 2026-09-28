package com.example.crypto.controller;

import com.example.crypto.service.EmailNotification;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RunJob {

    private static final Logger log = LoggerFactory.getLogger(RunJob.class);

    private final EmailNotification emailNotification;

    public RunJob(EmailNotification emailNotification) {
        this.emailNotification = emailNotification;
    }

    @GetMapping("/run")
    @SneakyThrows
    public String runJob() {

        log.info("Starting Job");

        emailNotification.sendWeeklyUpdate();

        log.info("Email sent successfully");

        return "Job completed";
    }
}