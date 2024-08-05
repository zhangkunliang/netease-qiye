package com.example.neteaseqiye.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${qiye.app.id}")
    private String appId;

    @Value("${qiye.auth.code}")
    private String authCode;

    @Value("${qiye.org.open.id}")
    private String orgOpenId;

    public String getAppId() {
        return appId;
    }

    public String getAuthCode() {
        return authCode;
    }

    public String getOrgOpenId() {
        return orgOpenId;
    }
}
