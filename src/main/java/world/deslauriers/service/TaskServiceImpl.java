package world.deslauriers.service;

import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import world.deslauriers.domain.Task;
import world.deslauriers.domain.TaskAllowance;
import world.deslauriers.repository.TaskAllowanceRepository;
import world.deslauriers.repository.TaskRepository;

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
}
