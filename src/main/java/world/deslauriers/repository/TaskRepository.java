package world.deslauriers.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Task;

@Repository
public interface TaskRepository extends ReactorCrudRepository<Task, Long> {

    Mono<Task> save(Task task);

}
