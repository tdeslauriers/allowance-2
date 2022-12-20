package world.deslauriers.repository;

import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Tasktype;
import world.deslauriers.service.dto.TaskDto;

@Repository
public interface TasktypeRepository extends ReactorCrudRepository<Tasktype, Long> {

    @Join(value = "tasktypeAllowances", type = Join.Type.LEFT_FETCH)
    @Join(value = "tasktypeAllowances.allowance", type = Join.Type.LEFT_FETCH)
    Flux<Tasktype> findByArchivedFalse();

    @Join(value = "tasktypeAllowances", type = Join.Type.LEFT_FETCH)
    @Join(value = "tasktypeAllowances.allowance", type = Join.Type.LEFT_FETCH)
    Mono<Tasktype> findByIdAndArchivedFalse(Long id);

    Mono<Tasktype> save(Tasktype tasktype);

    // using hibernate sql syntax
    // adhoc tasks need to show until complete, day over day, week over week
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
              LEFT JOIN tt.tasktypeAllowances tta
              LEFT JOIN tta.allowance a
            WHERE
                    a.id = :allowanceId
                 AND
                    (t.date = CURDATE()
                        OR
                                (tt.cadence = 'adhoc'
                            AND 
                                t.isComplete = false)
                    )
              """)
    Flux<TaskDto> findToDoList(Long allowanceId);

    // needed for daily task creation.
    @Query(value = """
            SELECT new world.deslauriers.domain.Tasktype(
              tt.id,
              tt.name,
              tt.cadence,
              tt.category)
            FROM Tasktype tt
              LEFT JOIN tt.tasktypeAllowances tta
              LEFT JOIN tta.allowance a
            WHERE
                tt.cadence = 'daily'
              AND
                a.id = :allowanceId
            """)
    Flux<Tasktype> findDailyTasktypes(Long allowanceId);
}
