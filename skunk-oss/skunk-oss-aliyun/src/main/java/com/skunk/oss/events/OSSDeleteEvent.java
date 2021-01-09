package com.skunk.oss.events;

import org.springframework.context.ApplicationEvent;

import com.skunk.oss.OSSFileStatus;

public class OSSDeleteEvent extends ApplicationEvent {

    private OSSFileStatus status;

    public OSSDeleteEvent(Object source, OSSFileStatus status) {
        super(source);
        this.status = status;
    }

    public OSSFileStatus getStatus() {
        return status;
    }
}
