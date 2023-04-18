package world.deslauriers.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.TaskAllowance;
import world.deslauriers.domain.Tasktype;
import world.deslauriers.service.dto.TaskDto;

public interface TasktypeService {
    Flux<Tasktype> getAllActive();

    Mono<Tasktype> getById(Long id);

    Flux<TaskAllowance> createDailyTasks();

    Mono<Tasktype> save(Tasktype cmd);

    Mono<Tasktype> update(Tasktype cmd);

    Flux<Tasktype> findDailyTasktypes(Long id);

    Flux<Tasktype> getAll();
}
