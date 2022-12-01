package world.deslauriers.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Tasktype;

public interface TasktypeService {
    Flux<Tasktype> findAll();

    Mono<Tasktype> save(Tasktype cmd);

    Mono<Tasktype> update(Tasktype cmd);
}
