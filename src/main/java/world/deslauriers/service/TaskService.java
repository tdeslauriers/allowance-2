package world.deslauriers.service;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Task;
import world.deslauriers.service.dto.CompleteQualityCmd;
import world.deslauriers.service.dto.DeleteRecordsDto;
import world.deslauriers.service.dto.TaskDto;

import java.time.LocalDateTime;

public interface TaskService {

    Mono<Task> updateComplete(CompleteQualityCmd cmd);

    Mono<Task> updateQuality(CompleteQualityCmd cmd);

    Flux<TaskDto> getDailyToDoList(String userUuid);

    Flux<TaskDto> getTasksFromPastWeek(String uuid);

    Mono<Task> save(Task task);

    Flux<Task> getAll();

    Flux<Task> getAllChangesSinceBackup(LocalDateTime lastBackup);

    Flux<DeleteRecordsDto> getDeletedRecords(LocalDateTime lastBackup, DeleteRecordsDto cleanup);
}
