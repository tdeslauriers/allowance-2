package world.deslauriers.service.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record BackupTasktype(
        String id,
        String name,
        String cadence,
        String category,
        String archived
) {
}