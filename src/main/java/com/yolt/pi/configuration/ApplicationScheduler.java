package com.yolt.pi.configuration;

import com.yolt.pi.common.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class ApplicationScheduler implements SchedulingConfigurer {

    private final List<Retry> retryableServices;
    private final ApplicationProperties properties;

    @EventListener(ApplicationReadyEvent.class)
    public void startScheduledRetryableServices() {
        if (!properties.getStartScheduler()) {
            log.warn("Scheduler is disabled");
            return;
        }

        ScheduledTaskRegistrar taskRegistrar = new ScheduledTaskRegistrar();
        for (Retry retryableService : retryableServices) {
            log.info("Scheduling service \"{}\" every {} [ms]", retryableService.getClass().getSimpleName(), retryableService.scheduleFixedRate());
            taskRegistrar.addFixedRateTask(retryableService::update, retryableService.scheduleFixedRate());
            configureTasks(taskRegistrar);
        }
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.afterPropertiesSet();
    }

    public void forceUpdateAll() {
        for (Retry retryableService : retryableServices) {
            log.info("Force updating service \"{}\"", retryableService.getClass().getSimpleName());
            retryableService.update();
        }
    }
}
