package world.deslauriers.service;

import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.TaskAllowance;
import world.deslauriers.repository.TaskAllowanceRepository;

import java.time.LocalDateTime;

@Singleton
public class TaskAllowanceServiceImpl implements TaskAllowanceService{

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
}
