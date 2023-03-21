package world.deslauriers.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDateTime;

@Serdeable
public class TaskDto{
        private Long id;
        private String name;
        private String cadence;
        private String category;
        private Byte archived;  // gateway jackson will serialize to boolean.

        @JsonFormat(pattern="yyyy-MM-dd")
        private LocalDateTime date;
        private Byte complete; // gateway jackson will serialize to boolean.
        private Byte satisfactory; // gateway jackson will serialize to boolean.

    public TaskDto() {
    }

    public TaskDto(Long id, String name, String cadence, String category, Byte archived, LocalDateTime date, Byte complete, Byte satisfactory) {
        this.id = id;
        this.name = name;
        this.cadence = cadence;
        this.category = category;
        this.archived = archived;
        this.date = date;
        this.complete = complete;
        this.satisfactory = satisfactory;
    }

    public TaskDto(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCadence() {
        return cadence;
    }

    public void setCadence(String cadence) {
        this.cadence = cadence;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Byte getArchived() {
        return archived;
    }

    public void setArchived(Byte archived) {
        this.archived = archived;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Byte getComplete() {
        return complete;
    }

    public void setComplete(Byte complete) {
        this.complete = complete;
    }

    public Byte getSatisfactory() {
        return satisfactory;
    }

    public void setSatisfactory(Byte satisfactory) {
        this.satisfactory = satisfactory;
    }
}
