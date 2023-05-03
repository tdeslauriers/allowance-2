package world.deslauriers.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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

    // endpoints take epoch time (unix seconds => Long)
    // so that chiller service can request only records
    // that have changed since last backup

    @Get("/allowances/{epoch}")
    public Flux<BackupAllowance> backupAllowances(Long epoch){
        return backupService.getAllowanceBackup(epoch);
    }

    @Get("/tasktypes/{epoch}")
    public Flux<BackupTasktype> backupTasktypes(Long epoch) {
        return backupService.getTasktypeBackup(epoch);
    }

    @Get("/tasks/{epoch}")
    public Flux<BackupTask> backupTasks(Long epoch){ return backupService.getTaskBackup(epoch); }

    @Get("/tasktype_allowances/{epoch}")
    public Flux<BackupTasktypeAllowance> backupTasktypeAllowance(Long epoch) { return backupService.getTasktypeAlowanceBackups(epoch); }

    @Get("/task_allowances/{epoch}")
    public Flux<BackupTaskAllowance> backupTaskAllowance(Long epoch) { return backupService.getTaskAlowanceBackups(epoch); }

    // clean up from row_end
    @Get("/cleanup/{epoch}")
    public Mono<DeleteRecordsDto> cleanupBackupRecords(Long epoch) {
        System.out.println(epoch);
        return backupService.cleanupBackupRecords(epoch); }
}
