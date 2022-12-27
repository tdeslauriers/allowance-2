package world.deslauriers.repository;

import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Task;
import world.deslauriers.domain.Tasktype;
import world.deslauriers.service.dto.TaskDto;

import java.time.LocalDate;

@Repository
public interface TaskRepository extends ReactorCrudRepository<Task, Long> {

    Mono<Task> save(Task task);

    @Join(value = "tasktype", type = Join.Type.LEFT_FETCH)
    Mono<Task> findById(Long id);

//    @Query(value = """
//            SELECT new world.deslauriers.service.dto.TaskDto(
//                t.id,
//                tt.name,
//                tt.cadence,
//                tt.category,
//                t.date,
//                t.isComplete,
//                t.isQuality)
//            FROM Task t
//                LEFT JOIN t.tasktype tt
//            WHERE
//                t.id = :id
//            """)
//    Mono<TaskDto> findTaskById(Long id);

    @Query(value = """
            SELECT new world.deslauriers.service.dto.TaskDto(
              t.id,
              tt.name,
              tt.cadence,
              tt.category,
              tt.archived,
              t.date,
              t.isComplete,
              t.isQuality)
            FROM Task t
              LEFT JOIN t.tasktype tt
              LEFT JOIN tt.tasktypeAllowances tta
              LEFT JOIN tta.allowance a
            WHERE
                    a.userUuid = :userUuid
                 AND
                    (t.date = CURDATE()
                        OR
                            (t.date BETWEEN CURDATE() AND :weekly)
                        OR
                            (tt.cadence = 'adhoc' AND t.isComplete = false)
                    )
              """)
    Flux<TaskDto> findToDoList(String userUuid, LocalDate weekly);
}
