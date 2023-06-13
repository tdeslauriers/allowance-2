package world.deslauriers.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@MappedEntity
public class TasktypeAllowance {

    @Id
    private Long id;

    @Nullable
    @JsonIgnore
    @Relation(Relation.Kind.MANY_TO_ONE)
    private Tasktype tasktype;

    @Nullable
    @Relation(Relation.Kind.MANY_TO_ONE)
    private Allowance allowance;

    public TasktypeAllowance() {
    }

    public TasktypeAllowance(@Nullable Tasktype tasktype, @Nullable Allowance allowance) {
        this.tasktype = tasktype;
        this.allowance = allowance;
    }

    public TasktypeAllowance(Long id, @Nullable Tasktype tasktype, @Nullable Allowance allowance) {
        this.id = id;
        this.tasktype = tasktype;
        this.allowance = allowance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Nullable
    public Tasktype getTasktype() {
        return tasktype;
    }

    public void setTasktype(@Nullable Tasktype tasktype) {
        this.tasktype = tasktype;
    }

    @Nullable
    public Allowance getAllowance() {
        return allowance;
    }

    public void setAllowance(@Nullable Allowance allowance) {
        this.allowance = allowance;
    }

}
