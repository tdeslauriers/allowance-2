package world.deslauriers.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.TaskAllowance;
import world.deslauriers.service.dto.CleanupDto;

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
                ta.id,
                MAX(ta.row_end) AS record_end
            FROM task_allowance FOR SYSTEM_TIME ALL ta
            GROUP BY ta.id
            HAVING record_end < NOW()
            """)
    Flux<CleanupDto> findDeletedRecords(LocalDateTime lastBackup);
}
