package com.skunk.oss;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectResult;
import com.skunk.core.utils.Objects2;
import com.skunk.oss.config.OssConfigProperties;
import com.skunk.oss.OSSFileStatus;
import com.skunk.oss.events.OSSDeleteEvent;
import com.skunk.oss.events.OSSUploadEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OSSClientService {

    @Resource(name = "ossThreadPoolTaskExecutor")
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    OssConfigProperties configProperties;

    @Resource
    OSS ossClient;

    @PostConstruct
    private void init() {
        log.info("Check OSS BucketName exists.");
        boolean exists = ossClient.doesBucketExist(configProperties.getBucketName());
        if (!exists) {
            ossClient.createBucket(configProperties.getBucketName());
        }
    }

    /**
     * @return
     */
    public String getBucketName() {
        return configProperties.getBucketName();
    }

    /**
     * 获取CDN路径
     *
     * @param ossKey
     *     文件�?
     */
    public String getCdnUrl(@NotBlank String ossKey) {

        Objects2.requireNotBlank(ossKey, "upload ossKey is null.");

        return configProperties.getCdnPath().concat("/" + ossKey);
    }

    /**
     * 上传文件 同步
     *
     * @param file
     *     上传的文�?
     * @param ossKey
     *     文件�?
     * @return 返回文件MD5
     */
    public String syncUploadFile(@NotNull File file, String ossKey) {
        Objects.requireNonNull(file, "upload file is null.");
        Objects2.requireNotBlank(ossKey, "upload ossKey is null.");

        try {
            PutObjectResult putObjectResult = ossClient.putObject(configProperties.getBucketName(), ossKey, file);
            log.debug("File Upload ETag:" + putObjectResult.getETag());
            return putObjectResult.getETag();
        } catch (OSSException oe) {
            throw new OSSException(oe.getErrorMessage());
        }
    }

    /**
     * 上传文件 异步
     *
     * @param file
     *     上传的文�?
     * @param ossKey
     *     文件�?
     * @param fileId
     *     事件相关的ID
     */
    public void asyncUploadFile(@NotNull File file, @NotBlank String ossKey, @NotBlank final String fileId) {

        Objects.requireNonNull(file, "upload file is null.");
        Objects2.requireNotBlank(ossKey, "upload ossKey is null.");
        Objects2.requireNotBlank(fileId, "upload fileId is null.");

        Optional.of(file).ifPresent(ossFile -> {
            threadPoolTaskExecutor.execute(() -> {
                log.info("OSSClientService file upload");
                // 已存在，不重复上�?
                boolean objectExist = ossClient.doesObjectExist(configProperties.getBucketName(), ossKey);
                if (objectExist) {
                    return;
                }
                try {
                    PutObjectResult putObjectResult = ossClient.putObject(configProperties.getBucketName(), ossKey, file);
                    log.info("upload file to oss message:{}", putObjectResult.toString());
                    eventPublisher.publishEvent(new OSSUploadEvent(fileId, file, OSSFileStatus.COMPLETE));
                } catch (OSSException e) {
                    log.error(e.getMessage(), e);
                    throw new OSSException(e.getErrorMessage());
                }
            });
        });
    }

    /**
     * 文件删除 同步
     *
     * @param ossKey
     *     文件�?
     */
    public void syncDeleteFile(@NotBlank String ossKey) {
        Objects2.requireNotBlank(ossKey);
        log.info("OSSClientService file upload");
        ossClient.deleteObject(configProperties.getBucketName(), ossKey);
    }

    /**
     * 文件删除 异步
     * <p>
     * 路径
     *
     * @param ossKey
     *     文件�?
     * @param fileId
     *     事件相关的ID
     */
    public void asyncDeleteFile(@NotBlank String ossKey, @NotBlank String fileId) {
        Objects2.requireNotBlank(ossKey);
        Objects2.requireNotBlank(fileId);

        log.info("OSSClientService file delete");
        threadPoolTaskExecutor.execute(() -> {
            ossClient.deleteObject(configProperties.getBucketName(), ossKey);
            eventPublisher.publishEvent(new OSSDeleteEvent(fileId, OSSFileStatus.DELETED));
        });
    }
}