package com.owl.chat_service.persistence.mongodb.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "messages")
public class Message {

    @Id
    private String id;

    /** Former FK: chat.id */
    private String chatId;

    /** Logical status flag */
    private Boolean status;
    
    public enum MessageState {
        ORIGIN,
        EDITED,
        REMOVED
    }
    private MessageState state;

    public enum MessageType {
        CHAT_NOTIFICATION,
        TEXT,
        IMG,
        VID,
        GENERIC_FILE
    }
    private MessageType type;

    /** Message body / payload */
    private String content;

    /** Former FK: user_profile.id */
    private String senderId;

    /** Self-reference flattened to ID */
    private String predecessorId;

    private Instant sentDate;

    private Instant removedDate;

    private Instant createdDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public MessageState getState() {
        return state;
    }

    public void setState(MessageState state) {
        this.state = state;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getPredecessorId() {
        return predecessorId;
    }

    public void setPredecessorId(String predecessorId) {
        this.predecessorId = predecessorId;
    }

    public Instant getSentDate() {
        return sentDate;
    }

    public void setSentDate(Instant sentDate) {
        this.sentDate = sentDate;
    }

    public Instant getRemovedDate() {
        return removedDate;
    }

    public void setRemovedDate(Instant removedDate) {
        this.removedDate = removedDate;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
}

