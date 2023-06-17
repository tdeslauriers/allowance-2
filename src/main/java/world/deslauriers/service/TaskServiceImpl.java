package world.deslauriers.service;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Task;
import world.deslauriers.repository.TaskRepository;
import world.deslauriers.service.dto.CompleteQualityCmd;
import world.deslauriers.service.dto.DeletedRecordsDto;
import world.deslauriers.service.dto.TaskDto;

import java.time.LocalDateTime;

@Singleton
public class TaskServiceImpl implements TaskService{

    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Flux<Task> getAll() { return taskRepository.findAll(); }

    @Override
    public Flux<Task> getAllChangesSinceBackup(LocalDateTime lastBackup) {
        return taskRepository.findAllChangesSinceBackup(lastBackup);
    }

    @Override
    public Flux<DeletedRecordsDto> getDeletedRecords(LocalDateTime lastBackup, DeletedRecordsDto cleanup) {

        return taskRepository
                .findDeletedRecords(lastBackup)
                .flatMap(deleted -> {
                    cleanup.getTaskIds().add(deleted.id());
                    return Mono.just(cleanup);
                })
                .switchIfEmpty(Flux.just(cleanup));
    }

    @Override
    public Mono<Task> getById(long taskId) {
        return taskRepository.findById(taskId);
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
    public Flux<TaskDto> getTasksFromPastWeek(String uuid){
        return taskRepository.findTasksFromPastWeek(uuid);
    }

    @Override
    public Mono<Task> save(Task task) {
        return taskRepository.save(task);
    }

}
