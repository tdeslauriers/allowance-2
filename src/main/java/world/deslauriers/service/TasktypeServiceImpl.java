package world.deslauriers.service;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.*;
import world.deslauriers.repository.TasktypeRepository;

import javax.validation.ValidationException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Objects;

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
    public Flux<Tasktype> getAllActive() {
        return tasktypeRepository.findByArchivedFalse().distinct();
    }

    @Override
    public Mono<Tasktype> getById(Long id){
        return tasktypeRepository.findByIdAndArchivedFalse(id);
    }


    @Override
    public Mono<Tasktype> findById(Long id){
        return tasktypeRepository.findById(id);
    }

    // local date must be tormorrow for utc offset
    @Override
    public Flux<Disposable> createDailyTasks() {
        return allowanceService.findAll()
                .map(allowance -> findDailyTasktypes(allowance.getId())
                        .map(tasktype -> taskService.save(new Task(OffsetDateTime.now(), false, false, tasktype))
                                .map(task -> taskAllowanceService.save(new TaskAllowance(task, allowance))
                                        .subscribe(taskAllowance -> log.info("Created task: {} - {}; assigned to {}",
                                                taskAllowance.getTask().getId(), task.getTasktype().getName(), taskAllowance.getAllowance().getUserUuid()))
                                ).subscribe()
                        ).subscribe());
    }

    @Override
    public Mono<Tasktype> save(Tasktype cmd) {

        if (!isValidCadence(cmd.getCadence())) throw new ValidationException("Incorrect cadence provided.");
        if (!isValidCategory(cmd.getCategory())) throw new ValidationException("Incorrect category provided.");

        return tasktypeRepository
                .save(new Tasktype(cmd.getName(), cmd.getCadence(), cmd.getCategory(), cmd.getArchived()))
                .map(tasktype -> {
                    // creates a task for adhoc's since they don't have scheduled creation.
                    if (tasktype.getCadence().toUpperCase().equals(Cadence.ADHOC.toString()) &&
                            cmd.getTasktypeAllowances() != null &&
                            cmd.getTasktypeAllowances().size() > 0){
                        var t = new Task(OffsetDateTime.now(), false, false, tasktype);
                        taskService.save(t)
                                .map(task -> {
                                    Flux.fromStream(cmd.getTasktypeAllowances().stream())
                                                .flatMap(tasktypeAllowance -> {
                                                    return taskAllowanceService.save(new TaskAllowance(task, tasktypeAllowance.getAllowance()));
                                                }).subscribe();
                                    return task;
                                }).subscribe();
                    }
                    return tasktype;
                })
                .map(tasktype -> {
                    cmd.setId(tasktype.getId());
                    if (cmd.getTasktypeAllowances() != null && cmd.getTasktypeAllowances().size() > 0) {
                        assignTasktypes(cmd).subscribe();
                    }
                    // gross hack to return data.
                    return cmd;
                });
    }

    @Override
    public Mono<Tasktype> update(Tasktype cmd) {

        if (!isValidCadence(cmd.getCadence())) throw new ValidationException("Incorrect cadence provided.");
        if (!isValidCategory(cmd.getCategory())) throw new ValidationException("Incorrect category provided.");

        return tasktypeRepository.update(new Tasktype(cmd.getId(), cmd.getName(), cmd.getCadence(), cmd.getCategory(), cmd.getArchived()))
                .map(tasktype -> {
                    if (cmd.getTasktypeAllowances() != null && cmd.getTasktypeAllowances().size() > 0){
                        assignTasktypes(cmd).subscribe();
                    }
                    // gross hack to retun data
                    return cmd;
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

    private Flux<TasktypeAllowance> assignTasktypes(Tasktype cmd){

       return Flux.fromStream(cmd.getTasktypeAllowances().stream()).flatMap(tasktypeAllowance -> tasktypeAllowanceService
                   .findByTasktypeAndAllowance(cmd, tasktypeAllowance.getAllowance())
                   .switchIfEmpty(tasktypeAllowanceService.save(new TasktypeAllowance(cmd, tasktypeAllowance.getAllowance()))));
    }


}
