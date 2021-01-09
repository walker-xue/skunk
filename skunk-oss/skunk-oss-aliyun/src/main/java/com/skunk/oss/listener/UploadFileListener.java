package com.skunk.oss.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.skunk.oss.data.OSSFileService;
import com.skunk.oss.OSSFileStatus;
import com.skunk.oss.events.OSSUploadEvent;

/**
 * @author walker
 */
@Service
public class UploadFileListener implements ApplicationListener<OSSUploadEvent> {

    @Autowired
    OSSFileService ossFileService;

    @Override
    public void onApplicationEvent(OSSUploadEvent uploadEvent) {

        if (OSSFileStatus.COMPLETE.equals(uploadEvent.getStatus())) {
            uploadEvent.getFile().delete();
        }
        ossFileService.updateStatus((String) uploadEvent.getSource(), uploadEvent.getStatus());
    }
}