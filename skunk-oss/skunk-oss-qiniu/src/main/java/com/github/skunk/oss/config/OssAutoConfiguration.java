package com.skunk.oss.config;

import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;


/**
 * @author walker
 * @since 2019年5月12日
 */
@Configuration
@EnableConfigurationProperties(OssConfigProperties.class)
public class OssAutoConfiguration {

    @Autowired
    OssConfigProperties configProperties;

    @Bean(name = "ossThreadPoolTaskExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(configProperties.getThreadPool().getCorePoolSize());
        executor.setMaxPoolSize(configProperties.getThreadPool().getMaxPoolSize());
        executor.setQueueCapacity(configProperties.getThreadPool().getQueueCapacity());
        executor.setKeepAliveSeconds(configProperties.getThreadPool().getKeepAliveSeconds());
        executor.setThreadNamePrefix("OSS Thread-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

    @Bean("qinyunAuth")
    public Auth getAuth() {
        String accessKey = configProperties.getAccessKeyId();
        String secretKey = configProperties.getAccessKeySecret();
        return Auth.create(accessKey, secretKey);
    }

    @Bean
    public UploadManager getUploadManager() {
        com.qiniu.storage.Configuration cfg = new com.qiniu.storage.Configuration(Zone.zone0());
        return new UploadManager(cfg);
    }

    @Bean
    public BucketManager getBucketManager(@Qualifier("qinyunAuth") Auth auth) {
        com.qiniu.storage.Configuration cfg = new com.qiniu.storage.Configuration(Zone.zone0());
        return new BucketManager(auth, cfg);
    }
}