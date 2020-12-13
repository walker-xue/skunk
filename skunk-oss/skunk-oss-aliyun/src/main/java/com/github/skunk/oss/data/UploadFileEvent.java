package com.skunk.oss.data;

import com.skunk.oss.data.domain.OssFile;
import com.skunk.oss.event.OssEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class UploadFileEvent implements OssEvent {

    @Autowired
    OssFileService ossFileService;
    @Autowired
    OssClientService OssClient;

    @Override
    public void onUpload(boolean uploadFlag, String fileId, File file, String fileMd5) {
        if (uploadFlag) {
            ossFileService.updateStatus(fileId, OssFile.UPLOADED);
            file.delete();
        } else {
            ossFileService.updateStatus(fileId, OssFile.FAILED);
        }
    }

    @Override
    public void onDelete(boolean deleteFlag, String id) {
        if (deleteFlag) {
            ossFileService.deleteById(id);
        }
    }
}