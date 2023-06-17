package world.deslauriers.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import io.micronaut.serde.annotation.Serdeable;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Serdeable
@MappedEntity
public class Tasktype {

    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String cadence;

    @NonNull
    private String category;

    @NotNull
    private Boolean archived;

    @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "tasktype")
    private Set<TasktypeAllowance> tasktypeAllowances;

    @JsonIgnore
    @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "tasktype")
    private Set<Task> tasks;

    public Tasktype() {
    }

    public Tasktype(@NonNull String name, @NonNull String cadence, @NonNull String category, @NotNull Boolean archived) {
        this.name = name;
        this.cadence = cadence;
        this.category = category;
        this.archived = archived;
    }

    public Tasktype(Long id, @NonNull String name, @NonNull String cadence, @NonNull String category, @NotNull Boolean archived) {
        this.id = id;
        this.name = name;
        this.cadence = cadence;
        this.category = category;
        this.archived = archived;
    }

    public Tasktype(Long id, @NonNull String name, @NonNull String cadence, @NonNull String category, @NotNull Boolean archived, Set<TasktypeAllowance> tasktypeAllowances, Set<Task> tasks) {
        this.id = id;
        this.name = name;
        this.cadence = cadence;
        this.category = category;
        this.archived = archived;
        this.tasktypeAllowances = tasktypeAllowances;
        this.tasks = tasks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getCadence() {
        return cadence;
    }

    public void setCadence(@NonNull String cadence) {
        this.cadence = cadence;
    }

    @NonNull
    public String getCategory() {
        return category;
    }

    public void setCategory(@NonNull String category) {
        this.category = category;
    }

    @NotNull
    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(@NotNull Boolean archived) {
        this.archived = archived;
    }

    public Set<TasktypeAllowance> getTasktypeAllowances() {
        return tasktypeAllowances;
    }

    public void setTasktypeAllowances(Set<TasktypeAllowance> tasktypeAllowances) {
        this.tasktypeAllowances = tasktypeAllowances;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return "Tasktype{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", cadence='" + cadence + '\'' +
               ", category='" + category + '\'' +
               ", archived=" + archived +
               ", tasktypeAllowances=" + tasktypeAllowances +
               ", tasks=" + tasks +
               '}';
    }
}
