package com.example.neteaseqiye.controller;

import com.example.neteaseqiye.entity.Message;
import com.example.neteaseqiye.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EmailController {
    @Value("${spring.mail.subscribe.account}")
    String accountName;
    @Value("${spring.mail.subscribe.domain}")
    String domain;
    @Value("${spring.mail.back.call.url}")
    String callUrl;
    @Value("${spring.mail.back.call.host}")
    String callHost;
    @Autowired
    private EmailService emailService;

    @RequestMapping("/subscribe")
    public String subscribe() {
//        String callbackUrl = "http://223.84.155.234:1998/api/path/handler";
        String callbackUrl = callUrl + ":" + callHost + "/api/path/handler";
        String accessToken = emailService.acquireToken();
        if (accessToken != null) {
            boolean dynamicSubscribeSuccess = emailService.addDynamicSubscribe(accessToken, callbackUrl);
            if (dynamicSubscribeSuccess) {
                boolean pushMailSubscribeSuccess = emailService.addPushMailSubscribe(accessToken, accountName, domain);
                if (pushMailSubscribeSuccess) {
                    return "Subscription successful";
                }
            }
        }
        return "Subscription failed";
    }

    @PostMapping("/path/handler")
    public String callback(@RequestBody Message message) {
        System.out.println(message);
        //回调后保存至数据库
        emailService.saveMessage(message);
        return "success";
    }

    @GetMapping("/findAll")
    public List<String> findAllEmailMessages() throws MessagingException {
        return emailService.getEmployeesWhoDidNotSendDailyReport();
    }

}
