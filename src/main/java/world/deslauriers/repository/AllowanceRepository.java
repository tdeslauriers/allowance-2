package world.deslauriers.repository;

import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Allowance;

@Repository
public interface AllowanceRepository extends ReactorCrudRepository<Allowance, Long> {

    Mono<Allowance> save(Allowance allowance);

    @Join(value = "tasktypeAllowances", type = Join.Type.LEFT_FETCH)
    @Join(value = "tasktypeAllowances.tasktype", type = Join.Type.LEFT_FETCH)
    Flux<Allowance> findAll();
}
