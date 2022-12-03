package world.deslauriers.service;

import io.micronaut.http.HttpResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Tasktype;
import world.deslauriers.domain.TasktypeAllowance;
import world.deslauriers.service.dto.AssignCmd;
import world.deslauriers.service.dto.TaskDto;

public interface TasktypeService {
    Flux<Tasktype> findAll();

    Flux<Tasktype> findDailyTasktypes(Long allowanceId);

    Mono<Tasktype> findById(Long id);

    Mono<Tasktype> save(Tasktype cmd);

    Mono<Tasktype> update(Tasktype cmd);

    Flux<TaskDto> getDailyTasks(Long allowanceId);

    Mono<TasktypeAllowance> assign(AssignCmd cmd);
}
