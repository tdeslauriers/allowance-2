package world.deslauriers.service;

import reactor.core.Disposable;

public interface TaskService {
    Disposable createDailyTasks();
}
