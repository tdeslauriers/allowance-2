package world.deslauriers.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.*;
import java.util.Set;

@Serdeable
@Entity
public class Tasktype {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String cadence;

    @NonNull
    private String category;

    private Boolean isArchived;

    @OneToMany(mappedBy = "tasktype")
    @JsonIgnore
    private Set<TasktypeAllowance> tasktypeAllowances;

    @JsonIgnore
    @OneToMany(mappedBy = "tasktype")
    private Set<Task> tasks;

    public Tasktype() {
    }

    public Tasktype(Long id, Boolean isArchived) {
        this.id = id;
        this.isArchived = isArchived;
    }

    public Tasktype(@NonNull String name, @NonNull String cadence, @NonNull String category) {
        this.name = name;
        this.cadence = cadence;
        this.category = category;
    }

    public Tasktype(Long id, @NonNull String name, @NonNull String cadence, @NonNull String category, Boolean isArchived) {
        this.id = id;
        this.name = name;
        this.cadence = cadence;
        this.category = category;
        this.isArchived = isArchived;
    }

    public Tasktype(Long id, @NonNull String name, @NonNull String cadence, @NonNull String category, Set<TasktypeAllowance> tasktypeAllowances, Set<Task> tasks) {
        this.id = id;
        this.name = name;
        this.cadence = cadence;
        this.category = category;
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

    public Boolean getArchived() {
        return isArchived;
    }

    public void setArchived(Boolean archived) {
        isArchived = archived;
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
}
