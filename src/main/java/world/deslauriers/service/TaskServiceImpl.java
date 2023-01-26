package world.deslauriers.service;

import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Task;
import world.deslauriers.repository.TaskRepository;
import world.deslauriers.service.dto.CompleteQualityCmd;
import world.deslauriers.service.dto.TaskDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

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
                .flatMap(task -> taskRepository.update(new Task(task.getId(), task.getDate(), cmd.status(), task.getSatisfactory(), task.getTasktype())));
    }

    @Override
    public Mono<Task> updateQuality(CompleteQualityCmd cmd) {
        return taskRepository
                .findById(cmd.taskId())
                .flatMap(task -> taskRepository.update(new Task(task.getId(), task.getDate(), task.getComplete(), cmd.status(), task.getTasktype())));
    }

    @Override
    public Flux<TaskDto> getDailyToDoList(String userUuid) {
        //query param of today's tasks -> today > 12.01 AM CST
        var startOfDay = OffsetDateTime.of(LocalDate.now(), LocalTime.of(0, 1, 0), ZoneOffset.of("-06:00"));
        return taskRepository.findToDoList(userUuid, startOfDay);

    }

    @Override
    public Mono<Task> save(Task task) {
        return taskRepository.save(task);
    }
}
