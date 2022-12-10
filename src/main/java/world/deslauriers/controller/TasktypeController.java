package world.deslauriers.controller;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Tasktype;
import world.deslauriers.domain.TasktypeAllowance;
import world.deslauriers.service.TasktypeService;
import world.deslauriers.service.dto.ArchiveCmd;
import world.deslauriers.service.dto.AssignCmd;
import world.deslauriers.service.dto.TaskDto;

import javax.validation.Valid;
import java.net.URI;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/tasktypes")
public class TasktypeController {

    protected final TasktypeService tasktypeService;

    public TasktypeController(TasktypeService tasktypeService) {
        this.tasktypeService = tasktypeService;
    }

    @Secured({"ALLOWANCE_ADMIN"})
    @Get
    Flux<Tasktype> getAll(){
        return tasktypeService.findAll();
    }

    @Secured({"ALLOWANCE_ADMIN"})
    @Get("/{id}")
    Flux<Tasktype> getById(Long id){
        return tasktypeService.findDailyTasktypes(id);
    }

    @Secured({"ALLOWANCE_ADMIN"})
    @Post
    Mono<HttpResponse<Tasktype>> save(@Body Tasktype cmd){
        return tasktypeService
                .save(cmd)
                .map(tasktype -> HttpResponse.created(tasktype)
                        .headers(headers -> headers.location(location(tasktype.getId()))));

    }

    @Secured({"ALLOWANCE_ADMIN"})
    @Put
    Mono<HttpResponse<Tasktype>> update(@Body @Valid Tasktype cmd){
        return tasktypeService
                .update(cmd)
                .map(tasktype -> HttpResponse
                        .<Tasktype>noContent()
                        .header(HttpHeaders.LOCATION, location(tasktype.getId()).getPath()));
    }

    @Secured({"ALLOWANCE_ADMIN"})
    @Put("/archive")
    Mono<HttpResponse<Tasktype>> archive(@Body ArchiveCmd cmd){
        return tasktypeService
                .findById(cmd.archiveId())
                .flatMap(tasktype -> {
                    tasktype.setArchived(true);
                    return tasktypeService.update(tasktype);
                })
                .map(tasktype -> HttpResponse
                        .noContent());
    }

    @Secured({"ALLOWANCE_ADMIN"})
    @Post("/assign")
    Mono<HttpResponse<TasktypeAllowance>> assignTask(@Body @Valid AssignCmd cmd){
        return tasktypeService
                .assign(cmd)
                .map(tasktypeAllowance -> HttpResponse
                        .noContent());

    }

    @Secured({"ALLOWANCE_ADMIN, ALLOWANCE_USER"})
    @Get("/daily/{allowanceId}")
    Flux<TaskDto> getDailyTasks(Long allowanceId){
        return tasktypeService.getDailyTasks(allowanceId);
    }

    protected URI location(Long id){
        return URI.create("/tasktypes/" + id);
    }

    protected URI location(Tasktype tasktype){
        return location(tasktype.getId());
    }
}
