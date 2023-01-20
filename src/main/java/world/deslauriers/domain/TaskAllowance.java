package world.deslauriers.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@MappedEntity
public class TaskAllowance {

    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    private Long id;

    @Nullable
    @Relation(Relation.Kind.MANY_TO_ONE)
    private Task task;

    @Nullable
    @Relation(Relation.Kind.MANY_TO_ONE)
    @JsonIgnore
    private Allowance allowance;

    public TaskAllowance() {
    }

    public TaskAllowance(@Nullable Task task, @Nullable Allowance allowance) {
        this.task = task;
        this.allowance = allowance;
    }

    public TaskAllowance(Long id, @Nullable Task task, @Nullable Allowance allowance) {
        this.id = id;
        this.task = task;
        this.allowance = allowance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Nullable
    public Task getTask() {
        return task;
    }

    public void setTask(@Nullable Task task) {
        this.task = task;
    }

    @Nullable
    public Allowance getAllowance() {
        return allowance;
    }

    public void setAllowance(@Nullable Allowance allowance) {
        this.allowance = allowance;
    }
}
