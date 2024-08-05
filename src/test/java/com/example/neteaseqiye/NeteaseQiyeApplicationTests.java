package com.example.neteaseqiye;

import com.example.neteaseqiye.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;
import java.util.ArrayList;

@SpringBootTest
class NeteaseQiyeApplicationTests {
    @Autowired
    private EmailService emailService;

    @Test
    void contextLoads() {
    }

    @Test
    void test() throws MessagingException {
        emailService.getEmployeesWhoDidNotSendDailyReport();
    }


}
