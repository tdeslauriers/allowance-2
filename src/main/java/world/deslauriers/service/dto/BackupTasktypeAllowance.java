package world.deslauriers.service.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record BackupTasktypeAllowance(
        Long id,
        String tasktypeId,
        String allowanceId
) {
}
