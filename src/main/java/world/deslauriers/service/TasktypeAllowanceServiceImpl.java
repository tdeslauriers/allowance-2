package world.deslauriers.service;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Allowance;
import world.deslauriers.domain.Tasktype;
import world.deslauriers.domain.TasktypeAllowance;
import world.deslauriers.repository.TasktypeAllowanceRepository;
import world.deslauriers.service.dto.RemoveTasktypeAllowanceCmd;

@Singleton
public class TasktypeAllowanceServiceImpl implements TasktypeAllowanceService{

    private static final Logger log = LoggerFactory.getLogger(TasktypeAllowanceServiceImpl.class);

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

    @Override
    public Mono<Void> removeTasktypeAllowance(RemoveTasktypeAllowanceCmd cmd) {

        return tasktypeAllowanceRepository.findByTasktypeIdAndAllowanceId(cmd.tasktypeId(), cmd.allowanceId())
                .flatMap(tasktypeAllowance -> {
                    log.info("Deleteing xref id: {} -- tasktype: {} <--> allowance {}", tasktypeAllowance.getId(), tasktypeAllowance.getTasktype().getName(), tasktypeAllowance.getAllowance().getUserUuid());
                    return tasktypeAllowanceRepository.delete(tasktypeAllowance);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.error("Attempted to delete xref TasktypeAllowance that does not exist");
                    return Mono.empty();
                }))
                .then();
    }
}
