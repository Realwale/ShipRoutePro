package com.shiproutepro.backend.mail.events.common;


import com.shiproutepro.backend.entities.AppUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class onApplicationEvent extends ApplicationEvent {

    private AppUser user;
    private String token;
    private EventType eventType;

    public onApplicationEvent(AppUser user, String token, EventType eventType) {
        super(user);
        this.user = user;
        this.token = token;
        this.eventType = eventType;
    }
}
