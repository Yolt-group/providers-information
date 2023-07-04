package com.yolt.pi.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Clock;
import java.util.concurrent.Executor;

@EnableScheduling
@EnableAsync
@EnableRetry
@Configuration
@ComponentScan("com.yolt.pi")
public class ApplicationConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public ServerCodecConfigurer serverCodecConfigurer() {
        return ServerCodecConfigurer.create();
    }

    @Bean
    public Executor asyncExecutor(
            @Value("${yolt.pi.corePoolSize}") Integer corePoolSize,
            @Value("${yolt.pi.maxPoolSize}") Integer maxPoolSize,
            @Value("${yolt.pi.queueCapacity}") Integer queueCapacity,
            @Value("${yolt.pi.awaitTerminationSeconds}") Integer awaitTerminationSeconds) {
        final ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setAwaitTerminationSeconds(awaitTerminationSeconds);
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.setThreadNamePrefix("async-provider-information-");
        return threadPoolTaskExecutor;
    }
}
