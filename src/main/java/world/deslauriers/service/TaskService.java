package world.deslauriers.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Task;
import world.deslauriers.domain.TaskAllowance;
import world.deslauriers.service.dto.CompleteQualityCmd;
import world.deslauriers.service.dto.TaskDto;

public interface TaskService {
    Flux<TaskAllowance> createDailyTasks();

    Mono<Task> updateComplete(CompleteQualityCmd cmd);

    Mono<Task> updateQuality(CompleteQualityCmd cmd);

    Mono<TaskDto> getById(Long id);
}
