package com.skunk.oss.events;

import java.io.File;

import org.springframework.context.ApplicationEvent;

import com.skunk.oss.OSSFileStatus;

import lombok.Getter;

/**
 * OSS file upload event
 *
 * @author walker
 */

public class OSSUploadEvent extends ApplicationEvent {

    @Getter
    private OSSFileStatus status;
    @Getter
    private File file;

    public OSSUploadEvent(Object source, File file, OSSFileStatus status) {
        super(source);
        this.status = status;
        this.file = file;
    }
}