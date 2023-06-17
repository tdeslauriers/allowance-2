package world.deslauriers.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.micronaut.data.annotation.*;
import io.micronaut.serde.annotation.Serdeable;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Set;

@Serdeable
@MappedEntity
public class Task {

    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    Long id;
    @JsonFormat(pattern="yyyy-MM-dd")
    @DateCreated
    @NotNull
    private OffsetDateTime date;
    @NotNull
    private Boolean complete;
    @NotNull
    private Boolean satisfactory;

    @Relation(Relation.Kind.MANY_TO_ONE)
    private Tasktype tasktype;

    @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "task")
    private Set<TaskAllowance> taskAllowances;

    public Task() {
    }

    public Task(OffsetDateTime date, Boolean complete, Boolean satisfactory) {
        this.date = date;
        this.complete = complete;
        this.satisfactory = satisfactory;
    }

    public Task(Long id, OffsetDateTime date, Boolean complete, Boolean satisfactory) {
        this.id = id;
        this.date = date;
        this.complete = complete;
        this.satisfactory = satisfactory;
    }

    public Task(OffsetDateTime date, Boolean complete, Boolean satisfactory, Tasktype tasktype) {
        this.date = date;
        this.complete = complete;
        this.satisfactory = satisfactory;
        this.tasktype = tasktype;
    }

    public Task(Long id, OffsetDateTime date, Boolean complete, Boolean satisfactory, Tasktype tasktype) {
        this.id = id;
        this.date = date;
        this.complete = complete;
        this.satisfactory = satisfactory;
        this.tasktype = tasktype;
    }

    public Task(Long id, OffsetDateTime date, Boolean complete, Boolean satisfactory, Tasktype tasktype, Set<TaskAllowance> taskAllowances) {
        this.id = id;
        this.date = date;
        this.complete = complete;
        this.satisfactory = satisfactory;
        this.tasktype = tasktype;
        this.taskAllowances = taskAllowances;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public Boolean getSatisfactory() {
        return satisfactory;
    }

    public void setSatisfactory(Boolean satisfactory) {
        this.satisfactory = satisfactory;
    }

    public Tasktype getTasktype() {
        return tasktype;
    }

    public void setTasktype(Tasktype tasktype) {
        this.tasktype = tasktype;
    }

    public Set<TaskAllowance> getTaskAllowances() {
        return taskAllowances;
    }

    public void setTaskAllowances(Set<TaskAllowance> taskAllowances) {
        this.taskAllowances = taskAllowances;
    }

    @Override
    public String toString() {
        return "Task{" +
               "id=" + id +
               ", date=" + date +
               ", complete=" + complete +
               ", satisfactory=" + satisfactory +
               ", tasktype=" + tasktype +
               ", taskAllowances=" + taskAllowances +
               '}';
    }
}
