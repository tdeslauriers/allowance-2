package world.deslauriers.service;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class Scheduler {

    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);
    private final TasktypeService tasktypeService;
    private final AllowanceService allowanceService;

    public Scheduler(TasktypeService tasktypeService, AllowanceService allowanceService) {
        this.tasktypeService = tasktypeService;
        this.allowanceService = allowanceService;
    }

    @Scheduled(cron = "0 5 6 * * *") // created at 6:05 AM UTC -> 12:05 AM CST, needs to let sunday job run first
    void dailyTasks(){
        tasktypeService.createDailyTasks().subscribe(taskAllowance -> log.info("Task {} successfully assigned to {}", taskAllowance.getTask().getId(), taskAllowance.getAllowance().getUserUuid()));
    }

    @Scheduled(cron = "0 1 6 * * SUN") // Sunday 6:01 AM UTC -> 12:01 AM CST
    void remit(){
        allowanceService.conductRemittance().subscribe(allowance -> log.info("Account {}'s updated to {}", allowance.getUserUuid(), allowance.getBalance()));
    }
}
