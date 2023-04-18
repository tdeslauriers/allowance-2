package world.deslauriers.service;

import reactor.core.publisher.Flux;
import world.deslauriers.service.dto.*;

public interface BackupService {
    Flux<BackupAllowance> getAllowanceBackup();

    Flux<BackupTasktype> getTasktypeBackup();

    Flux<BackupTask> getTaskBackup();

    Flux<BackupTasktypeAllowance> getTasktypeAlowanceBackups();

    Flux<BackupTaskAllowance> getTaskAlowanceBackups();
}
