package com.owl.chat_service.persistence.mongodb.document;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chat_members")
public class ChatMember {

    @Id
    private String id;

    /** Former FK: user_profile.id */
    private String memberId;

    /** Former FK: chat.id */
    private String chatId;

    public enum ChatMemberRole {
        OWNER,
        ADMIN,
        MEMBER,
        VIEWER
    }
    private ChatMemberRole role;

    private String nickname;

    /** Former FK: user_profile.id */
    private String inviterId;

    private Instant joinDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public ChatMemberRole getRole() {
        return role;
    }

    public void setRole(ChatMemberRole role) {
        this.role = role;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getInviterId() {
        return inviterId;
    }

    public void setInviterId(String inviterId) {
        this.inviterId = inviterId;
    }

    public Instant getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Instant joinDate) {
        this.joinDate = joinDate;
    }
}
