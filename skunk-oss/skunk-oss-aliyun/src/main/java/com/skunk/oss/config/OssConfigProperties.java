package com.skunk.oss.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author walker
 */
@lombok.Data
@ConfigurationProperties(prefix = "aliyun.oss")
public class OssConfigProperties {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String cdnPath;
    private ThreadPool threadPool = new ThreadPool();

    @lombok.Data
    public static class ThreadPool {

        private int corePoolSize = 5;
        private int maxPoolSize = 5;
        private int queueCapacity = 1000;
        private int keepAliveSeconds = 300;
    }

}