package com.owl.chat_service.persistence.mongodb.document;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chats")
public class Chat {

    @Id
    private String id;

    private Boolean status = true;

    public enum ChatType {
        PRIVATE,
        GROUP
    }
    private ChatType type;

    private String name;

    private String avatar;

    /** Former FK: user_profile.id */
    private String initiatorId;

    private String newestMessageId;

    private Instant newestMessageDate;

    private Instant createdDate;

    private Instant updatedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public ChatType getType() {
        return type;
    }

    public void setType(ChatType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getInitiatorId() {
        return initiatorId;
    }

    public void setInitiatorId(String initiatorId) {
        this.initiatorId = initiatorId;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Instant getNewestMessageDate() {
        return newestMessageDate;
    }

    public void setNewestMessageDate(Instant newestMessageDate) {
        this.newestMessageDate = newestMessageDate;
    }

    public String getNewestMessageId() {
        return newestMessageId;
    }

    public void setNewestMessageId(String newestMessageId) {
        this.newestMessageId = newestMessageId;
    }

}
