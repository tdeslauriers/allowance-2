package world.deslauriers.controller;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Task;
import world.deslauriers.service.TaskService;
import world.deslauriers.service.dto.CompleteQualityCmd;
import world.deslauriers.service.dto.TaskDto;

import javax.validation.Valid;
import java.net.URI;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Secured({"ALLOWANCE_USER"})
    @Get("/daily")
    Flux<TaskDto> getDailyTodoList(Authentication authentication){
        return taskService.getDailyToDoList(authentication.getAttributes().get("user_uuid").toString());
    }

    @Secured({"ALLOWANCE_ADMIN"})
    @Get("/daily/{uuid}")
    Flux<TaskDto> getUserDailyTodoList(String uuid){
        return taskService.getDailyToDoList(uuid);
    }

    @Secured({"ALLOWANCE_ADMIN", "ALLOWANCE_USER"})
    @Put("/complete")
    Mono<HttpResponse> updateIsComplete(@Body @Valid CompleteQualityCmd cmd){
        return taskService
                .updateComplete(cmd)
                .map(task -> HttpResponse
                        .noContent()
                        .header(HttpHeaders.LOCATION, location(task.getId()).getPath()));
    }

    @Secured({"ALLOWANCE_ADMIN"})
    @Put("/quality")
    Mono<HttpResponse> updateIsQuality(@Body @Valid CompleteQualityCmd cmd){
        return taskService
                .updateQuality(cmd)
                .map(task -> HttpResponse
                        .noContent()
                        .header(HttpHeaders.LOCATION, location(task.getId()).getPath()));
    }

    protected URI location(Long id){
        return URI.create("/tasks/" + id);
    }

    protected URI location(Task task){
        return location(task.getId());
    }
}
