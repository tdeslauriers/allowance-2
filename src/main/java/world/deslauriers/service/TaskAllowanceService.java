package world.deslauriers.service;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.TaskAllowance;
import world.deslauriers.service.dto.DeleteRecordsDto;

import java.time.LocalDateTime;

public interface TaskAllowanceService {
    Mono<TaskAllowance> save(TaskAllowance taskAllowance);

    Flux<TaskAllowance> getAllChangesSinceBackup(LocalDateTime lastBackup);

    Flux<DeleteRecordsDto> getDeletedRecords(LocalDateTime lastBackup, DeleteRecordsDto cleanup);
}
