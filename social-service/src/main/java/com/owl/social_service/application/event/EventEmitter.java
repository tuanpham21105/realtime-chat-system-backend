package com.owl.social_service.application.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventEmitter {
    private final ApplicationEventPublisher publisher;

    public EventEmitter(ApplicationEventPublisher publisher) {
        this.publisher = publisher;

    }

    public void emit(Event event) {
        publisher.publishEvent(event);
    }
}
