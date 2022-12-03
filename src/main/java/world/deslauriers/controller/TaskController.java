package world.deslauriers.controller;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Task;
import world.deslauriers.domain.Tasktype;
import world.deslauriers.service.TaskService;
import world.deslauriers.service.dto.CompleteQualityCmd;
import world.deslauriers.service.dto.TaskDto;

import javax.validation.Valid;
import java.net.URI;

@Controller("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Get("/{id}")
    Mono<TaskDto> getById(Long id){
        return taskService.getById(id);
    }

    @Put("/complete")
    Mono<HttpResponse> updateIsComplete(@Body @Valid CompleteQualityCmd cmd){
        return taskService
                .updateComplete(cmd)
                .map(task -> HttpResponse
                        .noContent()
                        .header(HttpHeaders.LOCATION, location(task.getId()).getPath()));
    }

    @Put("/quality")
    Mono<HttpResponse> updateIsCQuality(@Body @Valid CompleteQualityCmd cmd){
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
