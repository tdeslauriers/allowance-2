package world.deslauriers.repository;

import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Task;
import world.deslauriers.service.dto.TaskDto;

@Repository
public interface TaskRepository extends ReactorCrudRepository<Task, Long> {

    Mono<Task> save(Task task);

    @Join(value = "tasktype", type = Join.Type.LEFT_FETCH)
    Mono<Task> findById(Long id);

    @Query(value = """
            SELECT new world.deslauriers.service.dto.TaskDto(
                t.id,
                tt.name,
                tt.cadence,
                tt.category,
                t.date,
                t.isComplete,
                t.isQuality)
            FROM Task t
                LEFT JOIN t.tasktype tt
            WHERE
                t.id = :id
            """)
    Mono<TaskDto> findTaskById(Long id);
}
