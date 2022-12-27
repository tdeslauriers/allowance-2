package world.deslauriers.service;

import reactor.core.publisher.Mono;
import world.deslauriers.domain.Allowance;
import world.deslauriers.domain.Tasktype;
import world.deslauriers.domain.TasktypeAllowance;

public interface TasktypeAllowanceService {
    Mono<TasktypeAllowance> findByTasktypeAndAllowance(Tasktype tasktype, Allowance allowance);

    Mono<TasktypeAllowance> save(TasktypeAllowance tasktypeAllowance);
}
