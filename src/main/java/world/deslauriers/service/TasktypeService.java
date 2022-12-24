package world.deslauriers.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Tasktype;
import world.deslauriers.service.dto.TaskDto;

public interface TasktypeService {
    Flux<Tasktype> getAllActive();

    Mono<Tasktype> getById(Long id);

    Mono<Tasktype> findById(Long id);

    Mono<Tasktype> save(Tasktype cmd);

    Mono<Tasktype> update(Tasktype cmd);

    Flux<TaskDto> getDailyTasks(Long allowanceId);

    Flux<Tasktype> findDailyTasktypes(Long allowanceId);
}
