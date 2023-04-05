package world.deslauriers.repository;

import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Task;
import world.deslauriers.service.dto.TaskDto;

@R2dbcRepository(dialect = Dialect.MYSQL)
public interface TaskRepository extends ReactorCrudRepository<Task, Long> {

    Mono<Task> save(Task task);

    @Join(value = "tasktype", type = Join.Type.LEFT_FETCH)
    Mono<Task> findById(Long id);

    @Query(value = """
            SELECT
              t.id,
              tt.name,
              tt.cadence,
              tt.category,
              tt.archived,
              t.date,
              t.complete,
              t.satisfactory
            FROM task t
                LEFT JOIN tasktype tt ON t.tasktype_id = tt.id
                LEFT JOIN task_allowance ta ON t.id = ta.task_id
                LEFT JOIN allowance a ON ta.allowance_id = a.id
            WHERE
                a.user_uuid = :uuid
                AND
                    (t.date >= NOW() - INTERVAL 24 HOUR
                    OR
                    (t.date >= NOW() - INTERVAL 7 DAY - INTERVAL 6 HOUR
                        AND tt.cadence = 'Weekly')
                    OR
                    (tt.cadence = 'Adhoc'
                        AND t.complete = FALSE))
            """)
    Flux<TaskDto> findToDoList(String uuid);

    @Query(value = """
            SELECT
              t.id,
              tt.name,
              tt.cadence,
              tt.category,
              tt.archived,
              t.date,
              t.complete,
              t.satisfactory
            FROM task t
                LEFT JOIN tasktype tt ON t.tasktype_id = tt.id
                LEFT JOIN task_allowance ta ON t.id = ta.task_id
                LEFT JOIN allowance a ON ta.allowance_id = a.id
            WHERE
                a.user_uuid = :uuid
                AND
                    (t.date >= NOW() - INTERVAL 7 DAY - INTERVAL 6 HOUR
                    OR 
                    (tt.cadence = 'Adhoc'
                        AND t.complete = FALSE))
            """)
    Flux<TaskDto> findTasksFromPastWeek(String uuid);
}
