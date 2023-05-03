package world.deslauriers.service;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.TaskAllowance;
import world.deslauriers.repository.TaskAllowanceRepository;
import world.deslauriers.service.dto.DeleteRecordsDto;

import java.time.LocalDateTime;

@Singleton
public class TaskAllowanceServiceImpl implements TaskAllowanceService{

    private static final Logger log = LoggerFactory.getLogger(TaskAllowanceServiceImpl.class);

    private final TaskAllowanceRepository taskAllowanceRepository;

    public TaskAllowanceServiceImpl(TaskAllowanceRepository taskAllowanceRepository) {
        this.taskAllowanceRepository = taskAllowanceRepository;
    }

    @Override
    public Mono<TaskAllowance> save(TaskAllowance taskAllowance) {
        return taskAllowanceRepository.save(taskAllowance);
    }

    @Override
    public Flux<TaskAllowance> getAllChangesSinceBackup(LocalDateTime lastBackup) {
        return taskAllowanceRepository.findAllChangesSinceBackup(lastBackup);
    }

    @Override
    public Flux<DeleteRecordsDto> getDeletedRecords(LocalDateTime lastBackup, DeleteRecordsDto cleanup) {
        return taskAllowanceRepository
                .findDeletedRecords(lastBackup)
                .map(taskAllowance -> {
                    cleanup.getTaskAllowanceIds().add(taskAllowance.getId());
                    return cleanup;
                })
                .switchIfEmpty(Flux.just(cleanup));
    }
}
