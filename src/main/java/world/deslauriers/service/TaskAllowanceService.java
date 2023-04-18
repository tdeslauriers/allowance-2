package world.deslauriers.service;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.TaskAllowance;

public interface TaskAllowanceService {
    Mono<TaskAllowance> save(TaskAllowance taskAllowance);

    Flux<TaskAllowance> getAll();
}
