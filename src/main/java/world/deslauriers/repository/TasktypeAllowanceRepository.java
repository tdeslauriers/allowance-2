package world.deslauriers.repository;

import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import world.deslauriers.domain.TasktypeAllowance;

public interface TasktypeAllowanceRepository extends ReactorCrudRepository<TasktypeAllowance, Long> {


}
