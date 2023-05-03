package world.deslauriers.repository;

import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Tasktype;

import java.time.LocalDateTime;

import static io.micronaut.data.annotation.Join.Type.LEFT_FETCH;

@R2dbcRepository(dialect = Dialect.MYSQL)
public interface TasktypeRepository extends ReactorCrudRepository<Tasktype, Long> {

    @Join(value = "tasktypeAllowances", type = LEFT_FETCH)
    @Join(value = "tasktypeAllowances.allowance", type = LEFT_FETCH)
    Flux<Tasktype> findByArchivedFalse();

    @Join(value = "tasktypeAllowances", type = LEFT_FETCH)
    @Join(value = "tasktypeAllowances.allowance", type = LEFT_FETCH)
    Mono<Tasktype> findByIdAndArchivedFalse(Long id);

    Mono<Tasktype> save(Tasktype tasktype);

    @Query(value = """
            SELECT
                tt.id,
                tt.name,
                tt.cadence,
                tt.category,
                tt.archived
            FROM tasktype tt
                LEFT JOIN tasktype_allowance tta ON tt.id = tta.tasktype_id
                LEFT JOIN allowance a ON tta.allowance_id = a.id
            WHERE
                    tt.cadence = 'Daily'
                AND
                    tt.archived = false
                AND
                    a.id = :allowanceId
            """)
    Flux<Tasktype> findDailyTasktypes(Long allowanceId);

    @Query("""
            SELECT
                tt.id,
                tt.name,
                tt.cadence,
                tt.category,
                tt.archived
            FROM tasktype tt
            WHERE tt.row_start > :lastBackup
            """)
    Flux<Tasktype> findAllChangesSinceBackup(LocalDateTime lastBackup);

    @Query("""
            SELECT
                tt.id,
                tt.name,
                tt.cadence,
                tt.category,
                tt.archived
            FROM tasktype FOR SYSTEM_TIME AS OF TIMESTAMP :lastBackup tt
            WHERE tt.row_end < NOW()
            """)
    Flux<Tasktype> findDeletedRecords(LocalDateTime lastBackup);
}
