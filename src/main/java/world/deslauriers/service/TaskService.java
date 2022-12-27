package world.deslauriers.service;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Task;
import world.deslauriers.domain.TaskAllowance;
import world.deslauriers.domain.Tasktype;
import world.deslauriers.service.dto.CompleteQualityCmd;
import world.deslauriers.service.dto.TaskDto;

public interface TaskService {

    Mono<Task> updateComplete(CompleteQualityCmd cmd);

    Mono<Task> updateQuality(CompleteQualityCmd cmd);

    Flux<TaskDto> getDailyToDoList(String userUuid);
    Mono<Task> save(Task task);
}
