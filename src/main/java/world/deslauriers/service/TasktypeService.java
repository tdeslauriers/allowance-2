package world.deslauriers.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.TaskAllowance;
import world.deslauriers.domain.Tasktype;
import world.deslauriers.service.dto.DeletedRecordsDto;

import java.time.LocalDateTime;

public interface TasktypeService {
    Flux<Tasktype> getAllActive();

    Mono<Tasktype> getById(Long id);

    Flux<TaskAllowance> createDailyTasks();

    Mono<Tasktype> save(Tasktype cmd);

    Mono<Tasktype> update(Tasktype cmd);

    Flux<Tasktype> findDailyTasktypes(Long id);

    Flux<Tasktype> getAllChangesSinceBackup(LocalDateTime lastBackup);

    Flux<DeletedRecordsDto> getDeletedRecords(LocalDateTime lastBackup, DeletedRecordsDto cleanup);

    Mono<Tasktype> getByIdAll(long id);

    Mono<Tasktype> saveRestored(Tasktype tasktype);
}
