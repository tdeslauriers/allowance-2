package world.deslauriers.service;

import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Task;
import world.deslauriers.domain.TaskAllowance;
import world.deslauriers.repository.TaskAllowanceRepository;
import world.deslauriers.repository.TaskRepository;
import world.deslauriers.service.dto.CompleteQualityCmd;
import world.deslauriers.service.dto.TaskDto;

import java.time.LocalDate;

@Singleton
public class TaskServiceImpl implements TaskService{

    private final AllowanceService allowanceService;
    private final TaskRepository taskRepository;
    private final TaskAllowanceRepository taskAllowanceRepository;
    private final TasktypeService tasktypeService;

    public TaskServiceImpl(AllowanceService allowanceService, TaskRepository taskRepository, TaskAllowanceRepository taskAllowanceRepository, TasktypeService tasktypeService) {
        this.allowanceService = allowanceService;
        this.taskRepository = taskRepository;
        this.taskAllowanceRepository = taskAllowanceRepository;
        this.tasktypeService = tasktypeService;
    }

    @Override
    public Flux<TaskAllowance> createDailyTasks(){

        return allowanceService.findAll()
                .flatMap(allowance -> tasktypeService.findDailyTasktypes(allowance.getId())
                        .flatMap(tasktype -> taskRepository.save(new Task(LocalDate.now(), false, false, tasktype)))
                        .flatMap(task -> taskAllowanceRepository.save(new TaskAllowance(task, allowance))));
    }

    @Override
    public Mono<Task> updateComplete(CompleteQualityCmd cmd) {
        return taskRepository
                .findById(cmd.taskId())
                .flatMap(task -> taskRepository.update(new Task(task.getId(), task.getDate(), cmd.status(), task.getQuality(), task.getTasktype())));
    }

    @Override
    public Mono<Task> updateQuality(CompleteQualityCmd cmd) {
        return taskRepository
                .findById(cmd.taskId())
                .flatMap(task -> taskRepository.update(new Task(task.getId(), task.getDate(), task.getComplete(), cmd.status(), task.getTasktype())));
    }

    @Override
    public Mono<TaskDto> getById(Long id) {
        return taskRepository.findTaskById(id);
    }
}
