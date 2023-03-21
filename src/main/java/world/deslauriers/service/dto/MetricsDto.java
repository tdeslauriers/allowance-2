package world.deslauriers.service.dto;

import io.micronaut.serde.annotation.Serdeable;
import world.deslauriers.domain.Tasktype;

import java.util.Set;

@Serdeable
public record MetricsDto(
        String userUuid,
        Double balance,
        Integer total,
        Integer totalCompleted,
        Double percentageCompleted,
        Integer totalSatisfactory,
        Double percentageSatisfactory,
        Set<TaskDto> assigned
) {
}
