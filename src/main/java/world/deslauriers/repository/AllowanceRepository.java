package world.deslauriers.repository;

import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Allowance;

import java.time.LocalDateTime;

@R2dbcRepository(dialect = Dialect.MYSQL)
public interface AllowanceRepository extends ReactorCrudRepository<Allowance, Long> {

    Mono<Allowance> save(Allowance allowance);

    @Join(value = "tasktypeAllowances", type = Join.Type.LEFT_FETCH)
    @Join(value = "tasktypeAllowances.tasktype", type = Join.Type.LEFT_FETCH)
    Mono<Allowance> findById(Long id);

    @Join(value = "tasktypeAllowances", type = Join.Type.LEFT_FETCH)
    @Join(value = "tasktypeAllowances.tasktype", type = Join.Type.LEFT_FETCH)
    Mono<Allowance> findByUserUuid(String uuid);


    @Join(value = "tasktypeAllowances", type = Join.Type.LEFT_FETCH)
    @Join(value = "tasktypeAllowances.tasktype", type = Join.Type.LEFT_FETCH)
    @Join(value = "taskAllowances", type = Join.Type.LEFT_FETCH)
    @Join(value = "taskAllowances.task", type = Join.Type.LEFT_FETCH)
    Flux<Allowance> findAll();

    @Query("""
            SELECT
                a.id,
                a.balance,
                a.user_uuid
            FROM allowance a
            WHERE a.row_start > :lastBackup
            """)
    Flux<Allowance> findAllChangesSinceBackup(LocalDateTime lastBackup);

    @Query("""
            SELECT
                a. id,
                a.balance,
                a.user_uuid
            FROM allowance FOR SYSTEM_TIME AS OF TIMESTAMP :lastBackup a
            WHERE a.row_end < NOW()
            """)
    Flux<Allowance> findDeletedRecords(LocalDateTime lastBackup);
}
