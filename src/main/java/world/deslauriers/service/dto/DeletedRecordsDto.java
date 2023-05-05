package world.deslauriers.service.dto;

import io.micronaut.serde.annotation.Serdeable;

import java.util.HashSet;
import java.util.Set;

@Serdeable
public class DeletedRecordsDto {

    private Set<Long> allowanceIds = new HashSet<>();
    private Set<Long> tasktypeIds = new HashSet<>();
    private Set<Long> taskIds = new HashSet<>();
    private Set<Long> tasktypeAllowanceIds = new HashSet<>();
    private Set<Long> taskAllowanceIds = new HashSet<>();

    public DeletedRecordsDto() {
    }

    public DeletedRecordsDto(Set<Long> allowanceIds, Set<Long> tasktypeIds, Set<Long> taskIds, Set<Long> tasktypeAllowanceIds, Set<Long> taskAllowanceIds) {
        this.allowanceIds = allowanceIds;
        this.tasktypeIds = tasktypeIds;
        this.taskIds = taskIds;
        this.tasktypeAllowanceIds = tasktypeAllowanceIds;
        this.taskAllowanceIds = taskAllowanceIds;
    }

    public Set<Long> getAllowanceIds() {
        return allowanceIds;
    }

    public void setAllowanceIds(Set<Long> allowanceIds) {
        this.allowanceIds = allowanceIds;
    }

    public Set<Long> getTasktypeIds() {
        return tasktypeIds;
    }

    public void setTasktypeIds(Set<Long> tasktypeIds) {
        this.tasktypeIds = tasktypeIds;
    }

    public Set<Long> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(Set<Long> taskIds) {
        this.taskIds = taskIds;
    }

    public Set<Long> getTasktypeAllowanceIds() {
        return tasktypeAllowanceIds;
    }

    public void setTasktypeAllowanceIds(Set<Long> tasktypeAllowanceIds) {
        this.tasktypeAllowanceIds = tasktypeAllowanceIds;
    }

    public Set<Long> getTaskAllowanceIds() {
        return taskAllowanceIds;
    }

    public void setTaskAllowanceIds(Set<Long> taskAllowanceIds) {
        this.taskAllowanceIds = taskAllowanceIds;
    }

    @Override
    public String toString() {
        return "DeleteRecordsDto{" +
                "allowanceIds=" + allowanceIds +
                ", tasktypeIds=" + tasktypeIds +
                ", taskIds=" + taskIds +
                ", tasktypeAllowanceIds=" + tasktypeAllowanceIds +
                ", taskAllowanceIds=" + taskAllowanceIds +
                '}';
    }
}
