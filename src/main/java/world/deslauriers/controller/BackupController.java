package world.deslauriers.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import world.deslauriers.service.BackupService;
import world.deslauriers.service.dto.*;

@Secured({"COLD_STORAGE"})
@Controller("/backup")
public class BackupController {

    private static final Logger log = LoggerFactory.getLogger(BackupController.class);

    private final BackupService backupService;

    public BackupController(BackupService backupService) {
        this.backupService = backupService;
    }

    @Get("/allowances")
    public Flux<BackupAllowance> backupAllowances(){
        return backupService.getAllowanceBackup();
    }

    @Get("/tasktypes")
    public Flux<BackupTasktype> backupTasktypes() {
        return backupService.getTasktypeBackup();
    }

    @Get("/tasks")
    public Flux<BackupTask> backupTasks(){ return backupService.getTaskBackup(); }

    @Get("/tasktypeAllowances")
    public Flux<BackupTasktypeAllowance> backupTasktypeAllowance() { return backupService.getTasktypeAlowanceBackups(); }

    @Get("/taskAllowances")
    public Flux<BackupTaskAllowance> backupTaskAllowance() { return backupService.getTaskAlowanceBackups(); }
}
