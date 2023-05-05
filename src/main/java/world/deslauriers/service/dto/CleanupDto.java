package world.deslauriers.service.dto;

import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDateTime;

@Serdeable
public record CleanupDto(
        Long id,
        LocalDateTime recordEnd
) {
}
