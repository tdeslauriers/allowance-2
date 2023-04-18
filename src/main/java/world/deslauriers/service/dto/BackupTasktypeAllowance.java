package world.deslauriers.service.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record BackupTasktypeAllowance(
        String id,
        String tasktypeId,
        String allowanceId
) {
}
