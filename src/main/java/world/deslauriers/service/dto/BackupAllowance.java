package world.deslauriers.service.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record BackupAllowance(
        String userId,
        String balance,
        String userUuid
) {
}
