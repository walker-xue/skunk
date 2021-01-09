package com.skunk.oss.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.skunk.oss.data.OSSFileService;
import com.skunk.oss.OSSFileStatus;
import com.skunk.oss.events.OSSDeleteEvent;

/**
 * OSS delete listener
 *
 * @author walker
 */
@Component
public class OSSDeletListener implements ApplicationListener<OSSDeleteEvent> {

    @Autowired
    OSSFileService ossFileService;

    @Override
    public void onApplicationEvent(OSSDeleteEvent ossDeleteEvent) {

        if (OSSFileStatus.DELETED.equals(ossDeleteEvent.getStatus())) {
            ossFileService.deleteById((String) ossDeleteEvent.getSource());
        }
    }
}
