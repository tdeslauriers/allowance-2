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

    @OneToMany(mappedBy = "tasktype")
    @JsonIgnore
    private Set<TasktypeAllowance> tasktypeAllowances;

    @JsonIgnore
    @OneToMany(mappedBy = "tasktype")
    private Set<Task> tasks;

    public Tasktype() {
    }

    public Tasktype(@NonNull String name, @NonNull String cadence) {
        this.name = name;
        this.cadence = cadence;
    }

    public Tasktype(Long id, @NonNull String name, @NonNull String cadence) {
        this.id = id;
        this.name = name;
        this.cadence = cadence;
    }

    public Tasktype(Long id, @NonNull String name, @NonNull String cadence, Set<TasktypeAllowance> tasktypeAllowances, Set<Task> tasks) {
        this.id = id;
        this.name = name;
        this.cadence = cadence;
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
