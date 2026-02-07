package com.owl.chat_service.application.service.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.owl.chat_service.application.service.admin.chat.ControlChatAdminServices;
import com.owl.chat_service.application.service.admin.message.ControlMessageAdminServices;

@Component
public class EventsListener {
    private final ControlChatAdminServices controlChatAdminServices;
    private final ControlMessageAdminServices controlMessageAdminServices;

    public EventsListener(ControlChatAdminServices controlChatAdminServices, ControlMessageAdminServices controlMessageAdminServices) {
        this.controlChatAdminServices = controlChatAdminServices;
        this.controlMessageAdminServices = controlMessageAdminServices;
    }

    @EventListener(condition = "#event.name == 'ADD MESSAGE'")
    @Async
    public void handleAddMessageEvent(AddMessageEvent event) {
        controlChatAdminServices.updateChatNewestMessage(event.getMessage());
    }

    @EventListener(condition = "#event.name == 'ADD SYSTEM MESSAGE'")
    @Async
    public void handleAddSystemMessageEvent(SystemMessageEvent event) {
        controlMessageAdminServices.addNewSystemMessage(event.getChatId(), event.getContent());
    }
}
