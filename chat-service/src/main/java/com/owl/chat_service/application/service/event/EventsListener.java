package com.owl.chat_service.application.service.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.owl.chat_service.application.service.admin.chat.ControlChatAdminServices;

@Component
public class EventsListener {
    private final ControlChatAdminServices controlChatAdminServices;

    public EventsListener(ControlChatAdminServices controlChatAdminServices) {
        this.controlChatAdminServices = controlChatAdminServices;
    }

    @EventListener(condition = "#event.name == 'ADD MESSAGE'")
    @Async
    public void handleAddMessageEvent(AddMessageEvent event) {
        controlChatAdminServices.updateChatNewestMessage(event.getMessage());
    }
}
