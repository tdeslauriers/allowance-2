package world.deslauriers.service;

import reactor.core.publisher.Flux;
import world.deslauriers.service.dto.*;

// endpoints take epoch time (unix seconds => Long)
// so that chiller service can request only records
// that have changed since last backup
public interface BackupService {
    Flux<BackupAllowance> getAllowanceBackup(Long epoch);

    Flux<BackupTasktype> getTasktypeBackup(Long epoch);

    Flux<BackupTask> getTaskBackup(Long epoch);

    Flux<BackupTasktypeAllowance> getTasktypeAlowanceBackups(Long epoch);

    Flux<BackupTaskAllowance> getTaskAlowanceBackups(Long epoch);

    Flux<DeleteRecordsDto> cleanupBackupRecords(Long epoch);
}
