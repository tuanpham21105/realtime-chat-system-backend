package com.owl.chat_service.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "external.service.api")
public class ExternalServicesProperties {
    private String user;
    private String social;
    private String wsgateway;
    public String getWsgateway() {
        return wsgateway;
    }
    public void setWsgateway(String wsgateway) {
        this.wsgateway = wsgateway;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getSocial() {
        return social;
    }
    public void setSocial(String social) {
        this.social = social;
    }
}
