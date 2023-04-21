package world.deslauriers.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.TaskAllowance;

import java.time.LocalDateTime;

public interface TaskAllowanceService {
    Mono<TaskAllowance> save(TaskAllowance taskAllowance);

    Flux<TaskAllowance> getAllChangesSinceBackup(LocalDateTime lastBackup);
}
