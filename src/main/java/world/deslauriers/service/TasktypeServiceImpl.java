package world.deslauriers.service;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.*;
import world.deslauriers.repository.TasktypeRepository;
import world.deslauriers.service.dto.DeletedRecordsDto;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

@Singleton
public class TasktypeServiceImpl implements TasktypeService {

    private static final Logger log = LoggerFactory.getLogger(TasktypeServiceImpl.class);

    private final TasktypeRepository tasktypeRepository;
    private final TasktypeAllowanceService tasktypeAllowanceService;
    private final TaskAllowanceService taskAllowanceService;
    private final AllowanceService allowanceService;
    private final TaskService taskService;

    public TasktypeServiceImpl(TasktypeRepository tasktypeRepository, TasktypeAllowanceService tasktypeAllowanceService, TaskAllowanceService taskAllowanceService, AllowanceService allowanceService, TaskService taskService) {
        this.tasktypeRepository = tasktypeRepository;
        this.tasktypeAllowanceService = tasktypeAllowanceService;
        this.taskAllowanceService = taskAllowanceService;
        this.allowanceService = allowanceService;
        this.taskService = taskService;
    }

    @Override
    public Flux<Tasktype> getAllChangesSinceBackup(LocalDateTime lastBackup) {
        return tasktypeRepository.findAllChangesSinceBackup(lastBackup);
    }

    @Override
    public Flux<DeletedRecordsDto> getDeletedRecords(LocalDateTime lastBackup, DeletedRecordsDto cleanup) {
         return tasktypeRepository
                 .findDeletedRecords(lastBackup)
                 .flatMap(deleted -> {
                    cleanup.getTasktypeIds().add(deleted.id());
                    return Mono.just(cleanup);
                })
                 .switchIfEmpty(Flux.just(cleanup));
    }

    @Override
    public Mono<Tasktype> getByIdAll(long id) {
        return tasktypeRepository.findById(id);
    }

    @Override
    public Mono<Tasktype> saveRestored(Tasktype tasktype) {
        return tasktypeRepository.save(tasktype); // includes id
    }

    @Override
    public Flux<Tasktype> getAllActive() {
        return tasktypeRepository.findByArchivedFalse().distinct();
    }

    @Override
    public Mono<Tasktype> getById(Long id){
        return tasktypeRepository.findByIdAndArchivedFalse(id);
    }

    // local date must be tormorrow for utc offset
    @Override
    public Flux<TaskAllowance> createDailyTasks() {
        return allowanceService.findAll()
                .flatMap(allowance -> findDailyTasktypes(allowance.getId())
                    .flatMap(tasktype -> taskService.save(new Task(OffsetDateTime.now(), false, false, tasktype))
                        .flatMap(task -> {
                            log.info("Task {}: {} created; assigning to account {}", task.getId(), tasktype.getName(), allowance.getUserUuid());
                            return taskAllowanceService.save(new TaskAllowance(task, allowance));
                        })));
    }

    @Override
    public Mono<Tasktype> save(Tasktype cmd) {

        if (!isValidCadence(cmd.getCadence())) throw new ValidationException("Incorrect cadence provided.");
        if (!isValidCategory(cmd.getCategory())) throw new ValidationException("Incorrect category provided.");

        return tasktypeRepository.save(cmd)
                .flatMap(tasktype -> {
                    if (cmd.getTasktypeAllowances() != null && cmd.getTasktypeAllowances().size() > 0){
                        Flux.fromStream(cmd.getTasktypeAllowances().stream())
                                .flatMap(tasktypeAllowance -> allowanceService.getByUuid(tasktypeAllowance.getAllowance().getUserUuid()))
                                .flatMap(allowance -> {
                                    if (tasktype.getCadence().toUpperCase().equals(Cadence.ADHOC.toString())){
                                        return tasktypeAllowanceService.save(new TasktypeAllowance(tasktype, allowance))
                                                .flatMap(tasktypeAllowance -> {
                                                    log.info("Saved new xref TasktypeAllowance({}, Tasktype id: {}, Allowance id: {}", tasktypeAllowance.getId(), tasktype.getId(), allowance.getId());
                                                    return taskService.save(new Task(OffsetDateTime.now(), false, false, tasktype))
                                                            .flatMap(task -> {
                                                                log.info("Saved new adhoc Task id: {}", task.getId());
                                                                return taskAllowanceService.save(new TaskAllowance(task, allowance));
                                                            });
                                                });
                                    }
                                    return tasktypeAllowanceService.save(new TasktypeAllowance(tasktype, allowance));
                                })
                                .subscribe();
                    }
                    return Mono.just(tasktype);
                });
    }

    @Override
    public Mono<Tasktype> update(Tasktype cmd) {

        if (!isValidCadence(cmd.getCadence())) throw new ValidationException("Incorrect cadence provided.");
        if (!isValidCategory(cmd.getCategory())) throw new ValidationException("Incorrect category provided.");

        return tasktypeRepository
                .update(new Tasktype(cmd.getId(), cmd.getName(), cmd.getCadence(), cmd.getCategory(), cmd.getArchived()))
                .flatMap(tasktype -> {
                    if (cmd.getTasktypeAllowances() != null && cmd.getTasktypeAllowances().size() > 0){

                    }
                    // gross hack to return data
                    return Mono.just(cmd);
                });
    }

    @Override
    public Flux<Tasktype> findDailyTasktypes(Long allowanceId) {
        return tasktypeRepository.findDailyTasktypes(allowanceId);
    }

    private Boolean isValidCadence(String cadence){

        for (var c: Cadence.values()){
            if (Objects.equals(cadence.toUpperCase(), c.toString())){
                return true;
            }
        }
        return false;
    }

    private Boolean isValidCategory(String category){

        for (var c: Category.values()){
            if (Objects.equals(category.toUpperCase(), c.toString())){
                return true;
            }
        }
        return false;
    }
}
