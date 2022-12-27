package world.deslauriers.service;

import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Allowance;
import world.deslauriers.domain.Tasktype;
import world.deslauriers.domain.TasktypeAllowance;
import world.deslauriers.repository.TasktypeAllowanceRepository;

@Singleton
public class TasktypeAllowanceServiceImpl implements TasktypeAllowanceService{

    private final TasktypeAllowanceRepository tasktypeAllowanceRepository;

    public TasktypeAllowanceServiceImpl(TasktypeAllowanceRepository tasktypeAllowanceRepository) {
        this.tasktypeAllowanceRepository = tasktypeAllowanceRepository;
    }

    @Override
    public Mono<TasktypeAllowance> findByTasktypeAndAllowance(Tasktype tasktype, Allowance allowance) {
        return tasktypeAllowanceRepository.findByTasktypeAndAllowance(tasktype, allowance);
    }

    @Override
    public Mono<TasktypeAllowance> save(TasktypeAllowance tasktypeAllowance) {
        return tasktypeAllowanceRepository.save(tasktypeAllowance);
    }
}
