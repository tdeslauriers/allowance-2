package world.deslauriers.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import world.deslauriers.service.TasktypeService;
import world.deslauriers.service.dto.TasktypeBackup;

@Secured({"COLD_STORAGE"})
@Controller("/backup")
public class BackupController {

    private static final Logger log = LoggerFactory.getLogger(BackupController.class);

    private final TasktypeService tasktypeService;

    public BackupController(TasktypeService tasktypeService) {
        this.tasktypeService = tasktypeService;
    }

//    @Get
//    public Flux<TasktypeBackup> backup(){
//        return tasktypeService.backup();
//    }
}
