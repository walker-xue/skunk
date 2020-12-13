package com.skunk.oss.event;

import java.io.File;

/**
 * 上传后事�? 异步调用
 */
public interface OssEvent {
    /**
     * 文件新增事件
     *
     * @param uploadFlag true 上传成功 false上传失败
     * @param fileId     事件相关的ID
     * @param file       文件
     * @param fileMd5    文件MD%
     */
    void onUpload(boolean uploadFlag, String fileId, File file, String fileMd5);

    /**
     * 文件删除事件
     *
     * @param deleteFlag true 删除成功 false删除失败
     * @param id         事件相关的ID
     */
    void onDelete(boolean deleteFlag, String id);
}