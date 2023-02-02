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
              t.satisfactory,
              t.tasktype_id
            FROM task t
                LEFT JOIN tasktype tt ON t.tasktype_id = tt.id
                LEFT JOIN task_allowance ta ON t.id = ta.task_id
                LEFT JOIN allowance a ON ta.allowance_id = a.id
            WHERE
                a.user_uuid = :userUuid
                AND
                    t.date >= NOW() - INTERVAL 24 HOUR
                    OR
                    (t.date >= NOW() - INTERVAL 7 DAY - INTERVAL 6 HOUR
                        AND tt.cadence = 'Weekly')
                    OR
                    (tt.cadence = 'Adhoc'
                        AND t.complete = FALSE)

            """)
    Flux<TaskDto> findToDoList(String userUuid);

    @Query(value = """
            SELECT
                t.id,
                t.date,
                t.complete,
                t.satisfactory,
                t.tasktype_id
            FROM task t
                LEFT JOIN tasktype tt ON t.tasktype_id = tt.id
                LEFT JOIN task_allowance ta ON t.id = ta.task_id
                LEFT JOIN allowance a ON ta.allowance_id = a.id
            WHERE
                t.date >= NOW() - INTERVAL 7 DAY - INTERVAL 6 HOUR
                AND
                    (tt.cadence = 'Daily'
                        OR tt.cadence = 'Weekly')
                AND
                    a.user_uuid = :uuid        
            """)
    Flux<Task> findTasksFromPastWeek(String uuid);
}
