package world.deslauriers.service.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record BackupTask(
        String id,
        String date,
        String complete,
        String satisfactory,
        String tasktypeId
) {
}
