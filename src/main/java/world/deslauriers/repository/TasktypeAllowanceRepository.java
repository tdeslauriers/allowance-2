package world.deslauriers.repository;

import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Allowance;
import world.deslauriers.domain.Tasktype;
import world.deslauriers.domain.TasktypeAllowance;

@R2dbcRepository(dialect = Dialect.MYSQL)
public interface TasktypeAllowanceRepository extends ReactorCrudRepository<TasktypeAllowance, Long> {

    Mono<TasktypeAllowance> save(TasktypeAllowance tasktypeAllowance);

    Mono<TasktypeAllowance> findByTasktypeAndAllowance(Tasktype tasktype, Allowance allowance);
}
