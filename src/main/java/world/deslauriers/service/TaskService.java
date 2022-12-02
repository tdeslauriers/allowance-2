package world.deslauriers.service;

import reactor.core.publisher.Flux;
import world.deslauriers.domain.TaskAllowance;

public interface TaskService {
    Flux<TaskAllowance> createDailyTasks();
}
