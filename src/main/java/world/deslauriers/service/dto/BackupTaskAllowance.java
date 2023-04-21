package world.deslauriers.service.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record BackupTaskAllowance(
        Long id,
        String taskId,
        String allowanceId
) {
}
