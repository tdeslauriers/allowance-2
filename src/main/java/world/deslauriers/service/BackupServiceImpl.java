package world.deslauriers.service;

import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import world.deslauriers.service.dto.TasktypeBackup;

@Singleton
public class BackupServiceImpl implements BackupService {

    private final TasktypeService tasktypeService;

    public BackupServiceImpl(TasktypeService tasktypeService) {
        this.tasktypeService = tasktypeService;
    }


    @Override
    public Flux<TasktypeBackup> getBackupTasktypes() {
        return null;
    }
}
