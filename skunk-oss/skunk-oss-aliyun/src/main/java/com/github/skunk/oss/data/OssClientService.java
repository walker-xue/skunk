package com.skunk.oss.data;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectResult;
import com.skunk.core.exception.BaseException;
import com.skunk.oss.config.OssConfigProperties;
import com.skunk.oss.event.OssEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.Optional;

@Slf4j
@Service
public class OssClientService {

    @Resource(name = "ossThreadPoolTaskExecutor")
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    OssConfigProperties configProperties;

    @Resource
    OSS ossClient;

    public String getBucketName() {
        return configProperties.getBucketName();
    }

    /**
     * 获取CDN路径
     *
     * @param path 路径
     * @param key  文件�?
     */
    public String getCdnUrl(String key) {
        if (StringUtils.isNotEmpty(key)) {
            return configProperties.getCdnPath().concat("/" + key);
        }
        log.error("获取CDN路径失败 参数错误： key=" + key);
        throw new BaseException("获取CDN路径失败 参数错误：  key=" + key);
    }

    /**
     * 上传文件 同步
     *
     * @param file 上传的文�?
     * @param path 路径
     * @param key  文件�?
     * @return 返回文件MD5
     */
    public String syncUploadFile(File file, String ossKey) {
        if (file == null) {
            return null;
        }
        try {
            log.info("OSSClientService file upload");
            boolean exists = ossClient.doesBucketExist(configProperties.getBucketName());
            if (!exists) {
                ossClient.createBucket(configProperties.getBucketName());
            }
            PutObjectResult putObjectResult = ossClient.putObject(configProperties.getBucketName(), ossKey, file);
            log.debug("File Upload ETag:" + putObjectResult.getETag());
            return putObjectResult.getETag();
        } catch (OSSException oe) {
            log.error(oe.getMessage());
            return null;
        }
    }

    /**
     * 上传文件 异步
     *
     * @param file     上传的文�?
     * @param key      文件�?
     * @param oosEvent 上传成功时触�?
     * @param id       事件相关的ID
     */
    public void asyncUploadFile(File file, String key, OssEvent oosEvent, String id) {

        Optional.of(file).ifPresent(ossFile -> {

            boolean exists = ossClient.doesBucketExist(configProperties.getBucketName());
            if (!exists) {
                ossClient.createBucket(configProperties.getBucketName());
            }
            threadPoolTaskExecutor.execute(() -> {
                log.info("OSSClientService file upload");
                // 已存在，不重复上�?
                if (!ossClient.doesObjectExist(configProperties.getBucketName(), key)) {
                    try {
                        PutObjectResult putObjectResult = ossClient.putObject(configProperties.getBucketName(), key, file);
                        log.debug("File Upload ETag:" + putObjectResult.getETag());
                        oosEvent.onUpload(true, id, file, putObjectResult.getETag());
                    } catch (OSSException e) {
                        log.error(e.getMessage(), e);
                        throw new BaseException("文件同步失败");
                    }
                }
            });
        });
    }

    /**
     * 文件删除 同步
     *
     * @param path 路径
     * @param key  文件�?
     */
    public void syncDeleteFile(String key) {
        log.info("OSSClientService file upload");
        ossClient.deleteObject(configProperties.getBucketName(), key);
    }

    /**
     * 文件删除 异步
     *
     * @param path     路径
     * @param key      文件�?
     * @param oosEvent 上传成功时触�?
     * @param id       事件相关的ID
     */
    public void asyncDeleteFile(String key, OssEvent oosEvent, String id) {
        log.info("OSSClientService file delete");

        boolean exists = ossClient.doesBucketExist(configProperties.getBucketName());
        if (!exists) {
            ossClient.createBucket(configProperties.getBucketName());
        }
        threadPoolTaskExecutor.execute(() -> {
            ossClient.deleteObject(configProperties.getBucketName(), key);
            oosEvent.onDelete(true, id);
        });
    }
}