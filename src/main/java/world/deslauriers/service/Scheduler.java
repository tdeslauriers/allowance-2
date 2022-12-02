package world.deslauriers.service;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Disposable;

@Singleton
public class Scheduler {

    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);
    private final TaskService taskService;

    public Scheduler(TaskService taskService) {
        this.taskService = taskService;
    }

    @Scheduled(cron = "0 0 3 * * *")
    void dailyTasks(){

        log.info("Creating daily tasks.");
        taskService.createDailyTasks().subscribe();
    }
}
