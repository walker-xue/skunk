package com.github.skunk.oss.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

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
        executor.setThreadNamePrefix("oss thread-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

    @Bean(destroyMethod = "shutdown")
    public OSS client() {
        return new OSSClientBuilder().build(configProperties.getEndpoint(), configProperties.getAccessKeyId(), configProperties.getAccessKeySecret());
    }
}