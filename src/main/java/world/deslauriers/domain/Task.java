package world.deslauriers.domain;

import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Serdeable
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private LocalDate date;
    private Boolean isComplete;
    private Boolean isQuality;

    @ManyToOne
    private Tasktype tasktype;

    @OneToMany(mappedBy = "task")
    private Set<TaskAllowance> taskAllowances;

    public Task() {
    }

    public Task(LocalDate date, Boolean isComplete, Boolean isQuality) {
        this.date = date;
        this.isComplete = isComplete;
        this.isQuality = isQuality;
    }

    public Task(Long id, LocalDate date, Boolean isComplete, Boolean isQuality, Tasktype tasktype, Set<TaskAllowance> taskAllowances) {
        this.id = id;
        this.date = date;
        this.isComplete = isComplete;
        this.isQuality = isQuality;
        this.tasktype = tasktype;
        this.taskAllowances = taskAllowances;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean getComplete() {
        return isComplete;
    }

    public void setComplete(Boolean complete) {
        isComplete = complete;
    }

    public Boolean getQuality() {
        return isQuality;
    }

    public void setQuality(Boolean quality) {
        isQuality = quality;
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
}
