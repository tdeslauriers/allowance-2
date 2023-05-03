package world.deslauriers.repository;

import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Allowance;
import world.deslauriers.domain.Tasktype;
import world.deslauriers.domain.TasktypeAllowance;
import world.deslauriers.service.dto.DeleteRecordsDto;

import java.time.LocalDateTime;

@R2dbcRepository(dialect = Dialect.MYSQL)
public interface TasktypeAllowanceRepository extends ReactorCrudRepository<TasktypeAllowance, Long> {

    Mono<TasktypeAllowance> save(TasktypeAllowance tasktypeAllowance);

    Mono<TasktypeAllowance> findByTasktypeAndAllowance(Tasktype tasktype, Allowance allowance);

    @Join(value="tasktype", type = Join.Type.LEFT_FETCH)
    @Join(value = "allowance", type = Join.Type.LEFT_FETCH)
    Mono<TasktypeAllowance> findByTasktypeIdAndAllowanceId(Long tasktypeId, Long allowanceId);

    @Query("""
            SELECT
                *
            FROM tasktype_allowance tta
            WHERE tta.row_start > :lastBackup
            """)
    Flux<TasktypeAllowance> findAllChangesSinceBackup(LocalDateTime lastBackup);

    @Query("""
            SELECT
                *
            FROM tasktype_allowance tta
            WHERE
                tta.row_end < NOW()
                AND
                    tta.row_end > :lastBackup
            """)
    Flux<TasktypeAllowance> findDeletedRecords(LocalDateTime lastBackup);
}
