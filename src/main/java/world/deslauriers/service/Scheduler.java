package world.deslauriers.service;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class Scheduler {

    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);
    private final TasktypeService tasktypeService;

    public Scheduler(TasktypeService tasktypeService) {
        this.tasktypeService = tasktypeService;
    }

    @Scheduled(cron = "0 1 6 * * *") // created at 6:01 AM UTC -> 12:01 AM CST
    void dailyTasks(){
        tasktypeService.createDailyTasks().subscribe();
    }
}
