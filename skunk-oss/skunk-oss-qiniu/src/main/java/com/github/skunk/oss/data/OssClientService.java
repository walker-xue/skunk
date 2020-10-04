package com.github.skunk.oss.data;

import java.io.File;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.skunk.core.exception.BaseException;
import com.github.skunk.oss.config.OssConfigProperties;
import com.github.skunk.oss.event.OssEvent;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OssClientService {

    @Resource(name = "ossThreadPoolTaskExecutor")
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    OssConfigProperties configProperties;

    @Resource
    UploadManager uploadManager;

    @Resource
    BucketManager bucketManager;

    @Resource(name = "qinyunAuth")
    Auth qinyunAuth = null;

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
     * @param file   上传的文�?
     * @param ossKey 路径
     * @return 返回文件MD5
     */
    public String syncUploadFile(File file, String ossKey) {
        if (file == null) {
            return null;
        }
        try {
            log.info("OSSClientService file upload");
            String upToken = qinyunAuth.uploadToken(getBucketName());
            Response response = uploadManager.put(file, ossKey, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);

            log.debug(putRet.key);
            log.debug(putRet.hash);
            return putRet.key;
        } catch (QiniuException ex) {
            log.error(ex.getMessage(), ex);
            Response r = ex.response;
            log.error(r.toString());
            try {
                log.error(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
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

            //				boolean exists = ossClient.doesBucketExist(configProperties.getBucketName());
            //				if (!exists) {
            //					ossClient.createBucket(configProperties.getBucketName());
            //				}
            threadPoolTaskExecutor.execute(() -> {
                log.info("OSSClientService file upload");
                String eTag = syncUploadFile(file, key);
                // log.debug("File Upload ETag:" + putObjectResult.getETag());
                oosEvent.onUpload(true, id, file, eTag);
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
        try {
            bucketManager.delete(configProperties.getBucketName(), key);
        } catch (QiniuException e) {
            Response r = e.response;
            log.error(r.toString());
            try {
                log.error(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
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

        //		boolean exists = bucketManager.doesBucketExist(configProperties.getBucketName());
        //		if (!exists) {
        //			bucketManager.createBucket(configProperties.getBucketName());
        //		}
        threadPoolTaskExecutor.execute(() -> {
            try {
                bucketManager.delete(configProperties.getBucketName(), key);
                oosEvent.onDelete(true, id);
            } catch (QiniuException e) {
                Response r = e.response;
                log.error(r.toString());
                try {
                    log.error(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }

        });
    }
}