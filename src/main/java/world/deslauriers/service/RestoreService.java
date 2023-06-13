package world.deslauriers.service;

import reactor.core.publisher.Mono;
import world.deslauriers.domain.*;
import world.deslauriers.service.dto.*;

public interface RestoreService {
    Mono<Allowance> restoreAllowance(BackupAllowance backupAllowance);

    Mono<Tasktype> restoreTasktype(BackupTasktype backupTasktype);

    Mono<Task> restoreTask(BackupTask backupTask);

    Mono<TasktypeAllowance> restoreTasktypeAlloance(BackupTasktypeAllowance backupTasktypeAllowance);

    Mono<TaskAllowance> restoreTaskAlloance(BackupTaskAllowance backupTaskAllowance);
}
