package world.deslauriers.repository;

import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Tasktype;

@Repository
public interface TasktypeRepository extends ReactorCrudRepository<Tasktype, Long> {

    Mono<Tasktype> save(Tasktype tasktype);

}
