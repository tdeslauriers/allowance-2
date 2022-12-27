package world.deslauriers.service;

import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Task;
import world.deslauriers.domain.Tasktype;
import world.deslauriers.repository.TaskRepository;
import world.deslauriers.service.dto.CompleteQualityCmd;
import world.deslauriers.service.dto.TaskDto;

import java.time.LocalDate;

@Singleton
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
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
    public Flux<TaskDto> getDailyToDoList(String userUuid) {
        // added calc for last 7 days to include weekly tasks.
        // hibernate does not have mysql syntax of 'interval'
        return taskRepository.findToDoList(userUuid, LocalDate.now().minusDays(7L));
    }

    @Override
    public Mono<Task> save(Task task) {
        return taskRepository.save(task);
    }
}
