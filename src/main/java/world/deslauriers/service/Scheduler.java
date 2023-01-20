package world.deslauriers.service;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import world.deslauriers.domain.Task;

import java.time.LocalDate;

@Singleton
public class Scheduler {

    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);
    private final TasktypeService tasktypeService;
    private final TaskService taskService;
    private final AllowanceService allowanceService;

    public Scheduler(TasktypeService tasktypeService, TaskService taskService, AllowanceService allowanceService) {
        this.tasktypeService = tasktypeService;
        this.taskService = taskService;
        this.allowanceService = allowanceService;
    }

//    @Scheduled(fixedDelay = "5s")
    @Scheduled(cron = "0 0 4 * * *")
    void dailyTasks(){
        tasktypeService.createDailyTasks().subscribe();
    }
}
