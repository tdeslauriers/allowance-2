package world.deslauriers.controller;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Tasktype;
import world.deslauriers.service.TasktypeAllowanceService;
import world.deslauriers.service.TasktypeService;
import world.deslauriers.service.dto.RemoveTasktypeAllowanceCmd;

import javax.validation.Valid;
import java.net.URI;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/tasktypes")
public class TasktypeController {

    protected final TasktypeService tasktypeService;
    protected final TasktypeAllowanceService tasktypeAllowanceService;

    public TasktypeController(TasktypeService tasktypeService, TasktypeAllowanceService tasktypeAllowanceService) {
        this.tasktypeService = tasktypeService;
        this.tasktypeAllowanceService = tasktypeAllowanceService;
    }

    @Secured({"ALLOWANCE_ADMIN"})
    @Get
    Flux<Tasktype> getAll(){
        return tasktypeService.getAllActive();
    }

    @Secured({"ALLOWANCE_ADMIN"})
    @Get("/{id}")
    Mono<Tasktype> getById(Long id){
        return tasktypeService.getById(id);
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
    Mono<HttpResponse<Tasktype>> update(@Body Tasktype cmd){
        return tasktypeService
                .update(cmd)
                .map(tasktype -> HttpResponse
                        .ok(tasktype)
                        .header(HttpHeaders.LOCATION, location(tasktype.getId()).getPath()));
    }

    // Deletes xref, technically updating tasktype record => Put
    // request body needed.
    @Secured({"ALLOWANCE_ADMIN"})
    @Put("/remove/tasktype/allowance")
    Mono<HttpResponse<?>> removeTasktypeAllowance(@Body @Valid RemoveTasktypeAllowanceCmd cmd){
        return tasktypeAllowanceService.removeTasktypeAllowance(cmd)
                .thenReturn(HttpResponse
                        .noContent()
                        .header(HttpHeaders.LOCATION, URI.create("/remove/tasktype/allowance").getPath()));
    }

    protected URI location(Long id){
        return URI.create("/tasktypes/" + id);
    }

    protected URI location(Tasktype tasktype){
        return location(tasktype.getId());
    }
}
