package world.deslauriers.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Allowance;
import world.deslauriers.domain.Tasktype;
import world.deslauriers.domain.TasktypeAllowance;
import world.deslauriers.service.dto.RemoveTasktypeAllowanceCmd;

import java.time.LocalDateTime;

public interface TasktypeAllowanceService {
    Mono<TasktypeAllowance> findByTasktypeAndAllowance(Tasktype tasktype, Allowance allowance);

    Mono<TasktypeAllowance> save(TasktypeAllowance tasktypeAllowance);

    Mono<Void> removeTasktypeAllowance(RemoveTasktypeAllowanceCmd cmd);

    Flux<TasktypeAllowance> getAllChangesSinceBackup(LocalDateTime lastBackup);
}
