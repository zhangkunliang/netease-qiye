package com.example.neteaseqiye.task;

import com.example.neteaseqiye.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.mail.MessagingException;

@Configuration
@EnableScheduling
public class EmailStatisticsTask {

    @Autowired
    private EmailService emailService;

//    @Scheduled(cron = "0 0/1 * * * ?")
    @Scheduled(cron = "00 00 06 ? * TUE-SAT")
    public void reportDailyEmails() throws MessagingException {
        emailService.getEmployeesWhoDidNotSendDailyReport();
    }
}