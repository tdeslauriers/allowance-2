package world.deslauriers.service;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);
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

        return taskRepository.findToDoList(userUuid);

    }

    @Override
    public Mono<Task> save(Task task) {
        return taskRepository.save(task);
    }
}
