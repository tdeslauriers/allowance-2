package world.deslauriers.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.TaskAllowance;
import world.deslauriers.service.dto.DeleteRecordsDto;

import java.time.LocalDateTime;

@R2dbcRepository(dialect = Dialect.MYSQL)
public interface TaskAllowanceRepository extends ReactorCrudRepository<TaskAllowance, Long> {

    Mono<TaskAllowance> save(TaskAllowance taskAllowance);

    @Query("""
            SELECT
                *
            FROM task_allowance ta
            WHERE ta.row_start > :lastBackup
            """)
    Flux<TaskAllowance> findAllChangesSinceBackup(LocalDateTime lastBackup);

    @Query("""
            SELECT
                *
            FROM task_allowance ta
            WHERE
                ta.row_end < NOW()
                AND
                    ta.row_end > :lastBackup""")
    Flux<TaskAllowance> findDeletedRecords(LocalDateTime lastBackup);
}
