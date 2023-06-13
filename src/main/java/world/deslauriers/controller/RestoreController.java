package world.deslauriers.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import world.deslauriers.service.RestoreService;
import world.deslauriers.service.dto.*;

@Secured({"COLD_STORAGE"})
@Controller("/restore")
public class RestoreController {

    private static final Logger log = LoggerFactory.getLogger(RestoreController.class);

    private final RestoreService restoreService;

    public RestoreController(RestoreService restoreService) {
        this.restoreService = restoreService;
    }

    @Post("/allowance")
    public Mono<HttpResponse<?>> restoreAllowance(@Body BackupAllowance backupAllowance){
        return restoreService
                .restoreAllowance(backupAllowance)
                .thenReturn(HttpResponse.noContent());
    }

    @Post("/tasktype")
    public Mono<HttpResponse<?>> restoreTasktype(@Body BackupTasktype backupTasktype){
        return restoreService
                .restoreTasktype(backupTasktype)
                .thenReturn(HttpResponse.noContent());
    }

    @Post("/task")
    public Mono<HttpResponse<?>> restoreTask(@Body BackupTask backupTask){
        return restoreService
                .restoreTask(backupTask)
                .thenReturn(HttpResponse.noContent());
    }

    @Post("/tasktype_allowance")
    public Mono<HttpResponse<?>> restoreTasktypeAllowance(@Body BackupTasktypeAllowance backupTasktypeAllowance){
        return restoreService
                .restoreTasktypeAlloance(backupTasktypeAllowance)
                .thenReturn(HttpResponse.noContent());
    }

    @Post("/task_allowance")
    public Mono<HttpResponse<?>> restoreTaskAllowance(@Body BackupTaskAllowance backupTaskAllowance){
        return restoreService
                .restoreTaskAlloance(backupTaskAllowance)
                .thenReturn(HttpResponse.noContent());
    }
}
