package world.deslauriers.repository;

import io.micronaut.data.annotation.Join;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Allowance;

@R2dbcRepository(dialect = Dialect.MYSQL)
public interface AllowanceRepository extends ReactorCrudRepository<Allowance, Long> {

    Mono<Allowance> save(Allowance allowance);

    @Join(value = "tasktypeAllowances", type = Join.Type.LEFT_FETCH)
    @Join(value = "tasktypeAllowances.tasktype", type = Join.Type.LEFT_FETCH)
    Mono<Allowance> findById(Long id);

    @Join(value = "tasktypeAllowances", type = Join.Type.LEFT_FETCH)
    @Join(value = "tasktypeAllowances.tasktype", type = Join.Type.LEFT_FETCH)
    Mono<Allowance> findByUserUuid(String uuid);
}
