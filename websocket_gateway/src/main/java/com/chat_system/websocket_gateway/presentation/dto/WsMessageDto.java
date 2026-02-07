package com.chat_system.websocket_gateway.presentation.dto;

public class WsMessageDto {
    private String type;
    private String action;
    private Object data;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
    public WsMessageDto(String type, String action, Object data) {
        this.type = type;
        this.action = action;
        this.data = data;
    }
    
}
