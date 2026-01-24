package com.owl.social_service.persistence.mongodb.document;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "friendship")
public class Friendship {
    @Id
    private String id;

    private String firstUserId;
    
    private String secondUserId;

    private Instant createdDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstUserId() {
        return firstUserId;
    }

    public void setFirstUserId(String user_one_id) {
        this.firstUserId = user_one_id;
    }

    public String getSecondUserId() {
        return secondUserId;
    }

    public void setSecondUserId(String user_two_id) {
        this.secondUserId = user_two_id;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
}
