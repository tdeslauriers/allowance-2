package world.deslauriers.service;

import reactor.core.publisher.Flux;
import world.deslauriers.service.dto.TasktypeBackup;

public interface BackupService {
    Flux<TasktypeBackup> getBackupTasktypes();
}
