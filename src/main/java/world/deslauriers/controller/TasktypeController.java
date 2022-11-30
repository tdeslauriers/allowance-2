package world.deslauriers.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Tasktype;
import world.deslauriers.repository.TasktypeRepository;

import java.net.URI;

@Controller("/tasktypes")
public class TasktypeController {

    protected final TasktypeRepository tasktypeRepository;

    public TasktypeController(TasktypeRepository tasktypeRepository) {
        this.tasktypeRepository = tasktypeRepository;
    }

    @Get
    Flux<Tasktype> getAll(){
        return tasktypeRepository.findAll();
    }

    @Post
    Mono<HttpResponse<Tasktype>> save(@Body Tasktype cmd){
        return tasktypeRepository
                .save(cmd)
                .map(tasktype -> HttpResponse.created(tasktype)
                        .headers(headers -> headers.location(location(tasktype.getId()))));

    }

    protected URI location(Long id){
        return URI.create("/tasktypes/" + id);
    }

    protected URI location(Tasktype tasktype){
        return location(tasktype.getId());
    }
}
