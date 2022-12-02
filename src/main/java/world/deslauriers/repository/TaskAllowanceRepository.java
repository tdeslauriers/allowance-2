package world.deslauriers.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.TaskAllowance;

@Repository
public interface TaskAllowanceRepository extends ReactorCrudRepository<TaskAllowance, Long> {

    Mono<TaskAllowance> save(TaskAllowance taskAllowance);
}
